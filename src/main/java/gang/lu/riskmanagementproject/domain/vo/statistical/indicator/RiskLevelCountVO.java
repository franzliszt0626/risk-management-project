package gang.lu.riskmanagementproject.domain.vo.statistical.indicator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/7 14:06
 * @description 风险等级人数统计结果
 */
@Data
@ApiModel(description = "工人风险等级人数统计结果")
public class RiskLevelCountVO {
    @ApiModelProperty(value = "低风险人数", example = "50")
    private Integer lowRiskCount;

    @ApiModelProperty(value = "中风险人数", example = "15")
    private Integer mediumRiskCount;

    @ApiModelProperty(value = "高风险人数", example = "8")
    private Integer veryHighRiskCount;

    @ApiModelProperty(value = "严重风险人数", example = "3")
    private Integer highRiskCount;

    @ApiModelProperty(value = "总人数（去重后）", example = "76")
    private Integer totalCount;
}
