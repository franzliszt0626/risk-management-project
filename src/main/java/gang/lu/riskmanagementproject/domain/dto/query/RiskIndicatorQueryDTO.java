package gang.lu.riskmanagementproject.domain.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 14:19
 * @description 风险指标组合查询DTO（分页+多条件筛选）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "风险指标组合查询DTO（含分页参数）")
public class RiskIndicatorQueryDTO extends PageQueryDTO {
    @ApiModelProperty(value = "工人ID（精确匹配）", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "风险等级（低风险/中风险/高风险/严重风险）", example = "高风险")
    @ValidEnum(enumClass = RiskLevel.class, bizName = BusinessConstants.RISK_LEVEL)
    private String riskLevelValue;

    @ApiModelProperty(value = "是否报警（true=报警，false=未报警）", example = "true")
    private Boolean alertFlag;

    @ApiModelProperty(value = "心率最小值（bpm）", example = "60")
    @Range(min = 1, max = 300, message = "心率最小值需在1-300bpm范围内")
    private Integer minHeartRate;

    @ApiModelProperty(value = "心率最大值（bpm）", example = "120")
    @Range(min = 1, max = 300, message = "心率最大值需在1-300bpm范围内")
    private Integer maxHeartRate;

    @ApiModelProperty(value = "呼吸率最小值（次/min）", example = "10")
    @Range(min = 1, max = 60, message = "呼吸率最小值需在1-60次/min范围内")
    private Integer minRespiratoryRate;

    @ApiModelProperty(value = "呼吸率最大值（次/min）", example = "30")
    @Range(min = 1, max = 60, message = "呼吸率最大值需在1-60次/min范围内")
    private Integer maxRespiratoryRate;

    @ApiModelProperty(value = "疲劳百分比最小值（%）", example = "50")
    @Range(min = 0, max = 100, message = "疲劳百分比最小值需在0-100%范围内")
    private Double minFatiguePercent;

    @ApiModelProperty(value = "疲劳百分比最大值（%）", example = "90")
    @Range(min = 0, max = 100, message = "疲劳百分比最大值需在0-100%范围内")
    private Double maxFatiguePercent;

    @ApiModelProperty(value = "记录开始时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 00:00:00")
    @PastOrPresent(message = "记录开始时间不能晚于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recordStartTime;

    @ApiModelProperty(value = "记录结束时间（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 23:59:59")
    @PastOrPresent(message = "记录结束时间不能晚于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime recordEndTime;

}
