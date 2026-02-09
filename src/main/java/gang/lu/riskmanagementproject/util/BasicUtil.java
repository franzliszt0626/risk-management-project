package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

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

}
