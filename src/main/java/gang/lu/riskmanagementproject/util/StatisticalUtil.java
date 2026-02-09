package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.domain.vo.RiskTimePeriodCountVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 15:15
 * @description 统计工具类
 */
public class StatisticalUtil {

    private StatisticalUtil() {
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
     * 从List<Map>中根据s某字段获取count值
     */
    public static Integer getCountFromList(List<Map<String, Object>> dataList, String fieldName, String statusValue) {
        if (ObjectUtil.isEmpty(dataList)) {
            return 0;
        }
        // 遍历列表，找到匹配status的记录并返回count
        for (Map<String, Object> map : dataList) {
            String status = (String) map.get(fieldName);
            if (statusValue.equals(status)) {
                Number countNum = (Number) map.get("count");
                return ObjectUtil.isNull(countNum) ? 0 : countNum.intValue();
            }
        }
        // 没有匹配的状态返回0
        return 0;
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
