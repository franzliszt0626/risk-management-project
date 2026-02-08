package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import gang.lu.riskmanagementproject.common.FailureMessages;
import lombok.Getter;

import static gang.lu.riskmanagementproject.common.FailureMessages.RISK_PARAM_EMPTY_RISK_LEVEL;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:22
 * @description 风险等级，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum RiskLevel {
    /*
      风险程度，默认低风险
     */
    LOW_RISK("低风险"),
    MEDIUM_RISK("中风险"),
    HIGH_RISK("高风险"),
    VERY_HIGH_RISK("严重风险");

    @EnumValue
    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }

    /**
     * 工具方法：字符串转 RiskLevel 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RiskLevel fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(RISK_PARAM_EMPTY_RISK_LEVEL);
        }
        // 忽略大小写（增强兼容性）
        String trimValue = value.trim();
        for (RiskLevel level : values()) {
            if (level.value.equals(trimValue)) {
                return level;
            }
        }
        throw new IllegalArgumentException(
                String.format(FailureMessages.RISK_PARAM_INVALID_RISK_LEVEL, trimValue)
        );
    }
}
