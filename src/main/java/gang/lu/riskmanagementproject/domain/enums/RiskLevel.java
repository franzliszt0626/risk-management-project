package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

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
            throw new IllegalArgumentException("风险等级不能为空");
        }
        for (RiskLevel level : values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("无效的风险等级: [" + value + "]，允许值为：低风险、中风险、高风险、严重风险");
    }
}
