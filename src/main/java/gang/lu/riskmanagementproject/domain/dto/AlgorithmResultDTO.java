package gang.lu.riskmanagementproject.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:19
 * @description 用于传输算法数据
 */
@Data
public class AlgorithmResultDTO {
    @JsonProperty("heart_rate")
    private Integer heartRate;

    @JsonProperty("respiratory_rate")
    private Integer respiratoryRate;

    @JsonProperty("fatigue_percent")
    private Double fatiguePercent;

    /**
     * 低风险 / 中风险 / 高风险 / 严重风险
     */
    @JsonProperty("risk_level")
    private String riskLevel;

    /**
     * 0 or 1
     */
    @JsonProperty("alert_flag")
    private Boolean alertFlag;
}
