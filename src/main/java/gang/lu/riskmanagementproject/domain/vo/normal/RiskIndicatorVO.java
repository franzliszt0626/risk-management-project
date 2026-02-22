package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 风险指标视图对象，用于前端展示单次监测记录。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@ApiModel(description = "风险指标 - 视图对象")
public class RiskIndicatorVO {

    @ApiModelProperty(value = "关联工人 ID", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "心率（bpm）", example = "98")
    private Integer heartRate;

    @ApiModelProperty(value = "呼吸率（次/min）", example = "20")
    private Integer respiratoryRate;

    @ApiModelProperty(value = "疲劳百分比（%）", example = "72.5")
    private Double fatiguePercent;

    @ApiModelProperty(value = "综合风险等级（低风险 / 中风险 / 高风险 / 严重风险）",
            allowableValues = "低风险,中风险,高风险,严重风险", example = "高风险")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "是否触发报警", example = "true")
    private Boolean alertFlag;

    @ApiModelProperty(value = "记录创建时间", example = "2026-02-01 09:00:00")
    private LocalDateTime createTime;
}