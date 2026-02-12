package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 15:15
 * @description 统计工具类
 */
@Slf4j
public class StatisticalUtil {

    private static final String COUNT = "count";
    public static final String PERIOD = "period";

    private StatisticalUtil() {
    }

    /**
     * 从双层Map中安全获取count值
     */
    public static Integer getCountFromMap(Map<String, Map<String, Object>> dataMap, String key) {
        if (ObjectUtil.isEmpty(dataMap) || !dataMap.containsKey(key)) {
            return 0;
        }
        Map<String, Object> subMap = dataMap.get(key);
        return getCountFromSingleMap(subMap);
    }

    /**
     * 从List<Map>中获取count值（修复强转+流式简化）
     */
    public static Integer getCountFromList(List<Map<String, Object>> dataList, String fieldName, String fieldValue) {
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
     * 通用count值提取（解决强转问题，统一处理Number类型）
     */
    private static Integer getCountFromSingleMap(Map<String, Object> map) {
        if (ObjectUtil.isEmpty(map) || !map.containsKey(COUNT)) {
            return 0;
        }
        Object countObj = map.get(COUNT);
        if (countObj == null) {
            return 0;
        }
        // 兼容Integer/Long/BigDecimal等Number类型
        if (countObj instanceof Number) {
            return ((Number) countObj).intValue();
        }
        // 兼容字符串数字
        try {
            return Integer.parseInt(countObj.toString());
        } catch (NumberFormatException e) {
            log.warn("count值{}转换为Integer失败，返回0", countObj);
            return 0;
        }
    }


    /**
     * 构建时间段统计Item（保留原有逻辑，优化空值处理）
     */
    public static List<RiskTimePeriodCountVO.TimePeriodItem> buildTimePeriodItems(List<Map<String, Object>> periodList) {
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
     * 获取时间段描述（从RiskIndicatorImpl中抽取）
     */
    public static String getPeriodDesc(Integer period) {
        return switch (period) {
            case 0 -> "00:00-04:00";
            case 4 -> "04:00-08:00";
            case 8 -> "08:00-12:00";
            case 12 -> "12:00-16:00";
            case 16 -> "16:00-20:00";
            case 20 -> "20:00-24:00";
            default -> "未知时间段";
        };
    }

}
