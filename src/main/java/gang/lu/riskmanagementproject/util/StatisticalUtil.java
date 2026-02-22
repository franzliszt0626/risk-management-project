package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计数据处理工具类
 * <p>
 * 提供从 MyBatis 返回的 {@code Map} 结构中安全提取 count 值的方法，
 * 以及时间段统计 VO 的构建逻辑。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Slf4j
public class StatisticalUtil {

    private static final String COUNT = "count";
    public static final String PERIOD = "period";
    private static final String UNKNOWN_PERIOD = "未知时间段";

    /**
     * 时间段起始小时 → 描述文本。
     * 使用 Map 替代 switch，新增时间段只需在此处添加一行，不必修改方法体。
     */
    private static final Map<Integer, String> PERIOD_DESC_MAP;

    static {
        Map<Integer, String> map = new HashMap<>(8);
        map.put(0, "00:00-04:00");
        map.put(4, "04:00-08:00");
        map.put(8, "08:00-12:00");
        map.put(12, "12:00-16:00");
        map.put(16, "16:00-20:00");
        map.put(20, "20:00-24:00");
        PERIOD_DESC_MAP = Collections.unmodifiableMap(map);
    }

    private StatisticalUtil() {
        // 工具类禁止实例化
    }

    /**
     * 从双层 Map 中安全获取 count 值。
     * <p>
     * 适用于 MyBatis 返回 {@code Map<String, Map<String, Object>>} 的聚合查询结果。
     *
     * @param dataMap 外层 Map（key 为分组字段值，value 为含 count 的内层 Map）
     * @param key     外层 Map 的查找 key
     * @return count 值，key 不存在或 count 为 null 时返回 0
     */
    public static Integer getCountFromMap(Map<String, Map<String, Object>> dataMap, String key) {
        if (ObjectUtil.isEmpty(dataMap) || !dataMap.containsKey(key)) {
            return 0;
        }
        return getCountFromSingleMap(dataMap.get(key));
    }

    /**
     * 从 {@code List<Map>} 中按指定字段匹配并获取 count 值。
     * <p>
     * 适用于 MyBatis 返回 {@code List<Map<String, Object>>} 的 GROUP BY 查询结果。
     *
     * @param dataList   数据列表
     * @param fieldName  用于匹配的字段名
     * @param fieldValue 用于匹配的字段值
     * @return 匹配行的 count 值，未命中时返回 0
     */
    public static Integer getCountFromList(List<Map<String, Object>> dataList,
                                           String fieldName, String fieldValue) {
        if (ObjectUtil.isEmpty(dataList)) {
            return 0;
        }
        return dataList.stream()
                .filter(map -> fieldValue.equals(map.get(fieldName)))
                .findFirst()
                .map(StatisticalUtil::getCountFromSingleMap)
                .orElse(0);
    }

    /**
     * 将时间段原始数据列表转换为 {@link RiskTimePeriodCountVO.TimePeriodItem} 列表。
     *
     * @param periodList MyBatis 返回的时间段统计原始列表
     * @return 时间段统计 Item 列表，入参为空时返回空列表
     */
    public static List<RiskTimePeriodCountVO.TimePeriodItem> buildTimePeriodItems(
            List<Map<String, Object>> periodList) {
        if (ObjectUtil.isEmpty(periodList)) {
            return Collections.emptyList();
        }
        return periodList.stream()
                .map(itemMap -> {
                    Integer period = BasicUtil.safeLongToInt((Long) itemMap.get(PERIOD), -1);
                    Integer count = BasicUtil.safeLongToInt((Long) itemMap.get(COUNT), 0);
                    RiskTimePeriodCountVO.TimePeriodItem item = new RiskTimePeriodCountVO.TimePeriodItem();
                    item.setHighRiskCount(count);
                    item.setPeriodDesc(getPeriodDesc(period));
                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据时间段起始小时数获取描述文本。
     * <p>
     * 例如传入 {@code 8} 返回 {@code "08:00-12:00"}；
     * 传入 null 或未知值时返回 {@value #UNKNOWN_PERIOD}。
     *
     * @param period 时间段起始小时数（0 / 4 / 8 / 12 / 16 / 20）
     * @return 时间段描述字符串
     */
    public static String getPeriodDesc(Integer period) {
        if (period == null) {
            return UNKNOWN_PERIOD;
        }
        return PERIOD_DESC_MAP.getOrDefault(period, UNKNOWN_PERIOD);
    }

    /**
     * 从单个 Map 中提取 count 值，兼容 Integer / Long / BigDecimal 等 {@link Number} 子类
     * 以及字符串格式的数字，统一转为 int 返回。
     *
     * @param map 包含 {@code "count"} key 的 Map
     * @return count 值；count 字段缺失、为 null 或无法解析时返回 0
     */
    private static Integer getCountFromSingleMap(Map<String, Object> map) {
        if (ObjectUtil.isEmpty(map) || !map.containsKey(COUNT)) {
            return 0;
        }
        Object countObj = map.get(COUNT);
        if (countObj == null) {
            return 0;
        }
        if (countObj instanceof Number) {
            return ((Number) countObj).intValue();
        }
        try {
            return Integer.parseInt(countObj.toString());
        } catch (NumberFormatException e) {
            log.warn("[StatisticalUtil] count 值 '{}' 无法转换为 Integer，返回 0", countObj);
            return 0;
        }
    }
}