package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.exception.BizException;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/1 13:05
 * @description 参数校验工具
 */
public class ParameterVerifyUtil {


    /**
     * 疲劳率常量
     */
    private static final double MIN_FATIGUE_PERCENT = 0.0;
    private static final double MAX_FATIGUE_PERCENT = 100.0;

    /**
     * 校验心率
     * 规则：非空 && 大于0
     *
     * @param heartRate 心率值
     */
    public static void validateHeartRate(Integer heartRate) {
        if (ObjectUtil.isNull(heartRate) || heartRate <= 0) {
            throw new BizException(RISK_PARAM_INVALID_HEART_RATE);
        }
    }

    /**
     * 校验呼吸率
     * 规则：非空 && 大于0
     * 修复点：入参类型改为Double，与DTO定义一致
     *
     * @param respiratoryRate 呼吸率值
     */
    public static void validateRespiratoryRate(Integer respiratoryRate) {
        if (ObjectUtil.isNull(respiratoryRate) || respiratoryRate <= 0) {
            throw new BizException(String.format(RISK_PARAM_INVALID_RESPIRATORY_RATE));
        }
    }

    /**
     * 校验疲劳率
     * 规则：非空 && 0 ≤ 值 ≤ 100
     *
     * @param fatiguePercent 疲劳率值
     */
    public static void validateFatiguePercent(Double fatiguePercent) {
        if (ObjectUtil.isNull(fatiguePercent)
                || fatiguePercent < MIN_FATIGUE_PERCENT
                || fatiguePercent > MAX_FATIGUE_PERCENT) {
            throw new BizException(RISK_PARAM_INVALID_FATIGUE_PERCENT);
        }
    }

}
