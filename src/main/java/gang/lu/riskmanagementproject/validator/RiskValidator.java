package gang.lu.riskmanagementproject.validator;

import gang.lu.riskmanagementproject.annotation.ValidateLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static gang.lu.riskmanagementproject.common.EnumName.RISK_LEVEL;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 11:08
 * @description 风险的等级通用校验，兼容RiskLevel和AreaRiskLevel
 */
@Component
@RequiredArgsConstructor
public class RiskValidator {

    private final GeneralValidator generalValidator;

    /**
     * 通用风险等级枚举校验（泛型兼容RiskLevel/AreaRiskLevel）
     */
    @ValidateLog("风险等级枚举非空校验")
    public <E> void validateRiskLevel(E riskLevel, String businessScene) {
        generalValidator.validateEnumNotNull(riskLevel, RISK_LEVEL, businessScene);
    }
}
