package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * 工人生理风险等级枚举（由算法服务输出）。
 * <p>
 * 对应数据库 {@code t_risk_indicator.risk_level} 字段，风险程度由低到高依次为：
 * 低风险 → 中风险 → 高风险 → 严重风险。
 * 前端应通过下拉菜单选择，禁止用户自由输入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Getter
public enum RiskLevel implements ValueEnum<String>, IEnum<String> {

    /**
     * 低风险，生理指标正常
     */
    LOW_RISK("低风险"),

    /**
     * 中风险，生理指标轻度异常，需关注
     */
    MEDIUM_RISK("中风险"),

    /**
     * 高风险，生理指标明显异常，需干预
     */
    HIGH_RISK("高风险"),

    /**
     * 严重风险，生理指标严重异常，需立即处置
     */
    VERY_HIGH_RISK("严重风险");

    /**
     * 存储到数据库及序列化给前端的中文值
     */
    @JsonValue
    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }
}