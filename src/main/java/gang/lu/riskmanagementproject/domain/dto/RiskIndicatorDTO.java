package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:03
 * @description = 风险指标数据传输对象
 */


@Data
@ApiModel(value = "实时风险指标 - 传输对象")
public class RiskIndicatorDTO {
    @ApiModelProperty(value = "关联工人ID", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "心率 (bpm)", example = "98")
    private Integer heartRate;

    @ApiModelProperty(value = "心率变异性 SDNN (ms)", example = "25.6")
    private Double sdnnMs;

    @ApiModelProperty(value = "疲劳百分比 (%)", example = "78.5")
    private Double fatiguePercent;

    @ApiModelProperty(value = "当前风险等级")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "是否触发报警", example = "true")
    private Boolean alertFlag;

    @ApiModelProperty(value = "记录时间")
    private LocalDateTime recordTime;
}