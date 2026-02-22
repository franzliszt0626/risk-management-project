package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.property.MedicalProperty;
import gang.lu.riskmanagementproject.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/1 13:05
 * @description 参数校验工具
 */
@Component
@RequiredArgsConstructor
public class MedicalValidator {

    private final MedicalProperty medicalProperty;


    /**
     * 校验心率
     *
     * @param heartRate 心率值
     */
    public void validateHeartRate(Integer heartRate) {
        if (ObjectUtil.isNull(heartRate)) {
            throw new BizException(RISK_HEART_RATE_EMPTY);
        }
        if (heartRate < medicalProperty.getMinHeartRate() || heartRate > medicalProperty.getMaxHeartRate()) {
            throw new BizException(RISK_HEART_RATE_INVALID);
        }
    }

    /**
     * 校验呼吸率
     *
     * @param respiratoryRate 呼吸率值
     */
    public void validateRespiratoryRate(Integer respiratoryRate) {
        if (ObjectUtil.isNull(respiratoryRate)) {
            throw new BizException(RISK_RESPIRATORY_RATE_EMPTY);
        }
        if (respiratoryRate < medicalProperty.getMinRespiratoryRate() || respiratoryRate > medicalProperty.getMaxRespiratoryRate()) {
            throw new BizException(RISK_RESPIRATORY_RATE_INVALID);
        }
    }

    /**
     * 校验疲劳率
     * 规则：非空 && 0 ≤ 值 ≤ 100
     *
     * @param fatiguePercent 疲劳率值
     */
    public void validateFatiguePercent(Double fatiguePercent) {
        if (ObjectUtil.isNull(fatiguePercent)) {
            throw new BizException(RISK_FATIGUE_PERCENT_EMPTY);
        }
        if (fatiguePercent < medicalProperty.getMinFatiguePercent() || fatiguePercent > medicalProperty.getMaxFatiguePercent()) {
            throw new BizException(RISK_FATIGUE_PERCENT_INVALID);
        }
    }

}
