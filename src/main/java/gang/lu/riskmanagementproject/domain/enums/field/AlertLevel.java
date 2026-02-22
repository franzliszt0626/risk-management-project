package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 15:03
 * @description 预警记录等级枚举
 */
@Getter
public enum AlertLevel implements ValueEnum<String>, IEnum<String> {
    /**
     * 预警等级
     */
    WARNING("警告"),
    SERIOUS("严重");

    @JsonValue
    private final String value;

    AlertLevel(String value) {
        this.value = value;
    }
}
