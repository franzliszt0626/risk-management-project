package gang.lu.riskmanagementproject.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static gang.lu.riskmanagementproject.common.field.FieldEnglishConstants.*;

/**
 * 算法服务响应传输对象。
 * <p>
 * 由 Python 算法服务返回，字段命名遵循 snake_case，
 * 通过 {@link JsonProperty} 映射为 Java 驼峰命名。
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
@Data
@ApiModel(description = "算法服务分析结果")
public class AlgorithmResultDTO {

    @ApiModelProperty(value = "心率（bpm）", example = "98")
    @JsonProperty(HEART_RATE)
    private Integer heartRate;

    @ApiModelProperty(value = "呼吸率（次/min）", example = "20")
    @JsonProperty(RESPIRATORY_RATE)
    private Integer respiratoryRate;

    @ApiModelProperty(value = "疲劳百分比（%）", example = "72.5")
    @JsonProperty(FATIGUE_PERCENT)
    private Double fatiguePercent;

    @ApiModelProperty(value = "风险等级（低风险 / 中风险 / 高风险 / 严重风险）", example = "高风险")
    @JsonProperty(RISK_LEVEL)
    private String riskLevel;

    @ApiModelProperty(value = "是否触发报警", example = "true")
    @JsonProperty(ALERT_FLAG)
    private Boolean alertFlag;
}