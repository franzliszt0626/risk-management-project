package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:04
 * @description 风险数据前端展示类
 */


@Data
@ApiModel(description = "实时风险指标 - 视图对象")
public class RiskIndicatorVO {
    @ApiModelProperty("工人id")
    private Long workerId;

    @ApiModelProperty("工人心率")
    private Integer heartRate;

    @ApiModelProperty("工人呼吸率")
    private Integer respiratoryRate;

    @ApiModelProperty("工人综合疲劳指数")
    private Double fatiguePercent;

    @ApiModelProperty(value = "工人的风险等级", allowableValues = "低风险、中风险、高风险、严重风险")
    private RiskLevel riskLevel;

    @ApiModelProperty("是否标注为紧急")
    private Boolean alertFlag;

    @ApiModelProperty("本次记录的时间")
    private LocalDateTime createTime;
}
