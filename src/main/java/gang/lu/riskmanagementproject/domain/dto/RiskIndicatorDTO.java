package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

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
    @NotNull(message = "工人ID不能为空")
    @Positive(message = "工人ID必须为正整数")
    private Long workerId;

    @ApiModelProperty(value = "心率 (bpm)", required = true, example = "98")
    @NotNull(message = "心率不能为空")
    @PositiveOrZero(message = "心率不能为负数")
    @Range(min = 0, max = 300, message = "心率值需在0-300bpm范围内")
    private Integer heartRate;

    @ApiModelProperty(value = "呼吸率（次/min）", required = true, example = "25")
    @NotNull(message = "呼吸率不能为空")
    @PositiveOrZero(message = "呼吸率不能为负数")
    @Range(min = 0, max = 60, message = "呼吸率值需在0-60次/min范围内")
    private Integer respiratoryRate;

    @ApiModelProperty(value = "疲劳百分比 (%)", required = true, example = "78.5")
    @NotNull(message = "疲劳百分比不能为空")
    @Range(min = 0, max = 100, message = "疲劳百分比需在0-100%范围内")
    private Double fatiguePercent;

    @ApiModelProperty(value = "当前风险等级", required = true)
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "是否触发报警", required = true, example = "true")
    private Boolean alertFlag;

    @ApiModelProperty(value = "记录时间", example = "2026-02-09 14:30:00")
    private LocalDateTime recordTime;
}