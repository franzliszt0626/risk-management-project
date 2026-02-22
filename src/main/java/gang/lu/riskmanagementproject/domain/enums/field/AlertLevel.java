package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * 预警级别枚举。
 * <p>
 * 对应数据库 {@code t_alert_record.alert_level} 字段：{@code ENUM('警告', '严重')}。
 * 前端应通过下拉菜单选择，禁止用户自由输入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Getter
public enum AlertLevel implements ValueEnum<String>, IEnum<String> {

    /**
     * 普通预警，需关注但不紧急
     */
    WARNING("警告"),

    /**
     * 严重预警，需立即处理
     */
    SERIOUS("严重");

    /**
     * 存储到数据库及序列化给前端的中文值
     */
    @JsonValue
    private final String value;

    AlertLevel(String value) {
        this.value = value;
    }
}