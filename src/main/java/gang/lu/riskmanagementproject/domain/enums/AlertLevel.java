package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 15:03
 * @description 预警记录等级枚举
 */
@Getter
public enum AlertLevel implements ValueEnum<String> {
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
}
