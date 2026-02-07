package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.exception.BizException;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;
import static gang.lu.riskmanagementproject.common.MedicalConstants.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/1 13:05
 * @description 参数校验工具
 */
public class ParameterVerifyUtil {

    /**
     * 私有化构造器
     */
    private ParameterVerifyUtil() {
    }

    /**
     * 校验心率，只要心率不为负数或者null就可以
     *
     * @param heartRate 心率
     */
    public static void validateHeartRate(Integer heartRate) {
        if (ObjectUtil.isNull(heartRate) || heartRate <= 0) {
            throw new BizException(INVALID_HEARTRATE_ERROR_MESSAGE);
        }
    }

    /**
     * 校验呼吸率，同上
     *
     * @param respiratoryRate 呼吸率
     */
    public static void validateRespiratoryRate(Integer respiratoryRate) {
        if (ObjectUtil.isNull(respiratoryRate) || respiratoryRate <= 0) {
            throw new BizException(INVALID_RESPIRATORY_RATE_ERROR_MESSAGE);
        }
    }

    /**
     * 校验疲劳率，疲劳率应该在0-100之间
     *
     * @param fatiguePercent 疲劳率
     */
    public static void validateFatiguePercent(Double fatiguePercent) {
        if (ObjectUtil.isNull(fatiguePercent) || fatiguePercent < MIN_FATIGUE_PERCENT || fatiguePercent > MAX_FATIGUE_PERCENT) {
            throw new BizException(INVALID_FATIGUE_PERCENT_ERROR_MESSAGE);
        }
    }
}
