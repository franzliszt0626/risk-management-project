package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;

import java.util.Map;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/7 14:19
 * @description 统计相关方法
 */
public class StatisticUtil {
    private StatisticUtil() {
    }


    /**
     * 工具方法：从双层Map中提取count值，转换为Integer（默认0）
     */
    public static <T> Integer getCountFromMap(Map<T, Map<String, Object>> countMap, T riskLevel) {
        // 1. 获取该风险等级对应的子Map
        Map<String, Object> subMap = countMap.get(riskLevel);
        if (ObjectUtil.isNull(subMap)) {
            return 0;
        }
        // 2. 从子Map中提取count值，转换为Integer
        Object countObj = subMap.get("count");
        if (ObjectUtil.isNull(countObj)) {
            return 0;
        }
        // 兼容Long/Integer等数值类型
        return Integer.parseInt(countObj.toString());
    }

}
