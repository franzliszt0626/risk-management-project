package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.domain.vo.RiskTimePeriodCountVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 12:54
 * @description 基本工具类
 */
@Slf4j
public class BasicUtil {

    private BasicUtil() {
    }

    /**
     * 自定义分页参数处理（复用逻辑）
     */
    public static Integer[] handleCustomPageParams(Integer pageNum, Integer pageSize, String businessScene) {
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, 10);
        finalPageNum = Math.max(finalPageNum, 1);
        finalPageSize = Math.max(1, Math.min(finalPageSize, 50));
        log.info("{}分页参数处理完成：原始[{}，{}] → 处理后[{}，{}]", businessScene, pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }


    /**
     * 从Map中安全获取count值（统一处理类型转换）
     */
    public static Integer getCountFromMap(Map<String, Map<String, Object>> dataMap, String key) {
        if (ObjectUtil.isEmpty(dataMap) || !dataMap.containsKey(key)) {
            return 0;
        }
        Map<String, Object> subMap = dataMap.get(key);
        if (ObjectUtil.isEmpty(subMap) || !subMap.containsKey("count")) {
            return 0;
        }
        Number countNum = (Number) subMap.get("count");
        return ObjectUtil.isNull(countNum) ? 0 : countNum.intValue();
    }

    /**
     * 安全转换Long为Integer
     */
    public static Integer safeLongToInt(Long value, Integer defaultValue) {
        return ObjectUtil.isNull(value) ? defaultValue : value.intValue();
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


    /**
     * 构建时间段统计Item
     */
    public static List<RiskTimePeriodCountVO.TimePeriodItem> buildTimePeriodItems(List<Map<String, Object>> periodList) {
        List<RiskTimePeriodCountVO.TimePeriodItem> itemList = new ArrayList<>();
        for (Map<String, Object> itemMap : periodList) {
            Integer period = BasicUtil.safeLongToInt((Long) itemMap.get("period"), -1);
            Integer count = BasicUtil.safeLongToInt((Long) itemMap.get("count"), 0);

            RiskTimePeriodCountVO.TimePeriodItem item = new RiskTimePeriodCountVO.TimePeriodItem();
            item.setHighRiskCount(count);
            item.setPeriodDesc(BasicUtil.getPeriodDesc(period));
            itemList.add(item);
        }
        return itemList;
    }
}
