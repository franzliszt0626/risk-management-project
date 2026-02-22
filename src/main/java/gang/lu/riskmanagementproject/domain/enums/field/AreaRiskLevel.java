package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * 工作区域风险等级枚举。
 * <p>
 * 对应数据库 {@code t_work_area.area_risk_level} 字段。
 * 前端应通过下拉菜单选择，禁止用户自由输入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Getter
public enum AreaRiskLevel implements ValueEnum<String>, IEnum<String> {

    /**
     * 低风险区域，正常监控即可
     */
    LOW_RISK("低风险"),

    /**
     * 中风险区域，需加强巡检频率
     */
    MEDIUM_RISK("中风险"),

    /**
     * 高风险区域，需配备专项防护措施
     */
    HIGH_RISK("高风险");

    /**
     * 存储到数据库及序列化给前端的中文值
     */
    @JsonValue
    private final String value;

    AreaRiskLevel(String value) {
        this.value = value;
    }
}