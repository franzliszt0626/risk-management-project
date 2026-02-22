package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.property.MedicalProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 生理指标校验器
 * <p>
 * 校验心率、呼吸率、疲劳百分比等医疗参数的合法性，
 * 合法范围由 {@link MedicalProperty} 配置文件统一管理，方便在不修改代码的情况下调整阈值。
 *
 * @author Franz Liszt
 * @since 2026-02-01
 */
@Component
@RequiredArgsConstructor
public class MedicalValidator {

    private final MedicalProperty medicalProperty;

    /**
     * 校验心率。
     * <p>
     * 规则：非空 且 在 [{@code minHeartRate}, {@code maxHeartRate}] 范围内。
     *
     * @param heartRate 心率值（bpm）
     * @throws BizException 为 null 时抛出 400（空值），超出范围时抛出 400（范围无效）
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
     * 校验呼吸率。
     * <p>
     * 规则：非空 且 在 [{@code minRespiratoryRate}, {@code maxRespiratoryRate}] 范围内。
     *
     * @param respiratoryRate 呼吸率值（次/min）
     * @throws BizException 为 null 时抛出 400（空值），超出范围时抛出 400（范围无效）
     */
    public void validateRespiratoryRate(Integer respiratoryRate) {
        if (ObjectUtil.isNull(respiratoryRate)) {
            throw new BizException(RISK_RESPIRATORY_RATE_EMPTY);
        }
        if (respiratoryRate < medicalProperty.getMinRespiratoryRate()
                || respiratoryRate > medicalProperty.getMaxRespiratoryRate()) {
            throw new BizException(RISK_RESPIRATORY_RATE_INVALID);
        }
    }

    /**
     * 校验疲劳百分比。
     * <p>
     * 规则：非空 且 在 [{@code minFatiguePercent}, {@code maxFatiguePercent}] 范围内（通常 0 ~ 100）。
     *
     * @param fatiguePercent 疲劳百分比（%）
     * @throws BizException 为 null 时抛出 400（空值），超出范围时抛出 400（范围无效）
     */
    public void validateFatiguePercent(Double fatiguePercent) {
        if (ObjectUtil.isNull(fatiguePercent)) {
            throw new BizException(RISK_FATIGUE_PERCENT_EMPTY);
        }
        if (fatiguePercent < medicalProperty.getMinFatiguePercent()
                || fatiguePercent > medicalProperty.getMaxFatiguePercent()) {
            throw new BizException(RISK_FATIGUE_PERCENT_INVALID);
        }
    }
}