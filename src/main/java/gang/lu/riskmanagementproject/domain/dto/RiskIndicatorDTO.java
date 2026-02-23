package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.RISK_LEVEL;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.WORKER_ID;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 风险指标新增 / 修改传输对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "风险指标 - 传输对象")
public class RiskIndicatorDTO {

    @ApiModelProperty(value = "关联工人 ID", required = true, example = "1")
    @NotNull(message = WORKER_NOT_EXIST)
    @ValidId(bizName = WORKER_ID)
    private Long workerId;

    @ApiModelProperty(value = "心率（bpm）", required = true, example = "98")
    @NotNull(message = RISK_HEART_RATE_EMPTY)
    @Range(min = 1, max = 300, message = RISK_HEART_RATE_INVALID)
    private Integer heartRate;

    @ApiModelProperty(value = "呼吸率（次/min）", required = true, example = "25")
    @NotNull(message = RISK_RESPIRATORY_RATE_EMPTY)
    @Range(min = 1, max = 60, message = RISK_RESPIRATORY_RATE_INVALID)
    private Integer respiratoryRate;

    @ApiModelProperty(value = "疲劳百分比（%）", required = true, example = "78.5")
    @NotNull(message = RISK_FATIGUE_PERCENT_EMPTY)
    @DecimalMin(value = "0.0", message = RISK_FATIGUE_PERCENT_INVALID)
    @DecimalMax(value = "100.0", message = RISK_FATIGUE_PERCENT_INVALID)
    private Double fatiguePercent;

    @ApiModelProperty(value = "风险等级（低风险 / 中风险 / 高风险 / 严重风险）", required = true, example = "高风险")
    @NotBlank(message = RISK_LEVEL_EMPTY)
    @ValidEnum(enumClass = RiskLevel.class, bizName = RISK_LEVEL)
    private String riskLevelValue;

    @ApiModelProperty(value = "是否触发报警", required = true, example = "true")
    @NotNull(message = RISK_LEVEL_EMPTY)
    private Boolean alertFlag;
}