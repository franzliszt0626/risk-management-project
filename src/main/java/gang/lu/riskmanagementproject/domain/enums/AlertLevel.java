package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import gang.lu.riskmanagementproject.common.FailureMessages;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 15:03
 * @description 预警记录等级枚举
 */
@Getter
public enum AlertLevel {
    /**
     * 预警等级
     */
    WARNING("警告"),
    SERIOUS("严重");

    @EnumValue
    private final String value;

    AlertLevel(String value) {
        this.value = value;
    }

    /**
     * 工具方法：判断传来的String格式的字段是否合法
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AlertLevel fromValue(String value) {
        // 等级为空
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(FailureMessages.ALERT_PARAM_EMPTY_LEVEL);
        }
        String trimValue = value.trim();
        for (AlertLevel level : values()) {
            if (level.value.equals(trimValue)) {
                return level;
            }
        }
        // 抛出具体的非法参数异常
        throw new IllegalArgumentException(String.format(FailureMessages.ALERT_PARAM_INVALID_LEVEL, trimValue));
    }

    /**
     * 校验预警等级是否合法（保留原布尔值校验逻辑，兼容旧代码）
     */
    @Deprecated
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String trimValue = value.trim();
        for (AlertLevel level : values()) {
            if (level.value.equals(trimValue)) {
                return true;
            }
        }
        return false;
    }
}
