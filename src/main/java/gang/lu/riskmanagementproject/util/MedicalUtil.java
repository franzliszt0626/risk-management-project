package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.config.MedicalConfig;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/1 13:05
 * @description 参数校验工具
 */
@Component
public class MedicalUtil {

    private MedicalUtil() {
    }

    /**
     * 常量定义（与业务规则对齐）
     */
    private static final double MIN_FATIGUE_PERCENT = 0.0;
    private static final double MAX_FATIGUE_PERCENT = 100.0;
    private static final int MIN_HEART_RATE = MedicalConfig.DEFAULT_MIN_HEART_RATE;
    private static final int MAX_HEART_RATE = MedicalConfig.DEFAULT_MAX_HEART_RATE;
    private static final int MIN_RESPIRATORY_RATE = MedicalConfig.DEFAULT_MIN_RESPIRATORY_RATE;
    private static final int MAX_RESPIRATORY_RATE = MedicalConfig.DEFAULT_MAX_RESPIRATORY_RATE;

    /**
     * 校验心率
     *
     * @param heartRate 心率值
     */
    public static void validateHeartRate(Integer heartRate) {
        if (ObjectUtil.isNull(heartRate)) {
            throw new BizException(RISK_HEART_RATE_EMPTY);
        }
        if (heartRate < MIN_HEART_RATE || heartRate > MAX_HEART_RATE) {
            throw new BizException(RISK_HEART_RATE_INVALID);
        }
    }

    /**
     * 校验呼吸率
     *
     * @param respiratoryRate 呼吸率值
     */
    public static void validateRespiratoryRate(Integer respiratoryRate) {
        if (ObjectUtil.isNull(respiratoryRate)) {
            throw new BizException(RISK_RESPIRATORY_RATE_EMPTY);
        }
        if (respiratoryRate < MIN_RESPIRATORY_RATE || respiratoryRate > MAX_RESPIRATORY_RATE) {
            throw new BizException(RISK_RESPIRATORY_RATE_INVALID);
        }
    }

    /**
     * 校验疲劳率
     * 规则：非空 && 0 ≤ 值 ≤ 100
     *
     * @param fatiguePercent 疲劳率值
     */
    public static void validateFatiguePercent(Double fatiguePercent) {
        if (ObjectUtil.isNull(fatiguePercent)) {
            throw new BizException(RISK_FATIGUE_PERCENT_EMPTY);
        }
        if (fatiguePercent < MIN_FATIGUE_PERCENT || fatiguePercent > MAX_FATIGUE_PERCENT) {
            throw new BizException(RISK_FATIGUE_PERCENT_INVALID);
        }
    }

}
