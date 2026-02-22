package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-09 20:45:18
 * @description 风险指标数据传输对象
 */
@Data
@ApiModel(value = "实时风险指标 - 传输对象")
public class RiskIndicatorDTO {
    @ApiModelProperty(value = "关联工人ID，允许重复", required = true, example = "1")
    @ValidId(bizName = BusinessConstants.WORKER_ID)
    private Long workerId;

    @ApiModelProperty(value = "心率 (bpm)", required = true, example = "98")
    @Range(min = 1, max = 300, message = RISK_HEART_RATE_INVALID)
    private Integer heartRate;

    @ApiModelProperty(value = "呼吸率（次/min）", required = true, example = "25")
    @Range(min = 1, max = 60, message = RISK_RESPIRATORY_RATE_INVALID)
    private Integer respiratoryRate;

    @ApiModelProperty(value = "疲劳百分比 (%)", required = true, example = "78.5")
    @DecimalMin(value = "0.0", message = RISK_FATIGUE_PERCENT_INVALID)
    @DecimalMax(value = "100.0", message = RISK_FATIGUE_PERCENT_INVALID)
    private Double fatiguePercent;

    @ApiModelProperty(value = "当前风险等级", required = true)
    @ValidEnum(enumClass = RiskLevel.class, bizName = BusinessConstants.RISK_LEVEL)
    private String riskLevelValue;

    @ApiModelProperty(value = "是否触发报警", required = true, example = "true")
    private Boolean alertFlag;
}