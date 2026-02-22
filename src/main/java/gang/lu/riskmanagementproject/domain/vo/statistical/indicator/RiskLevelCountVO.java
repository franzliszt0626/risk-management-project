package gang.lu.riskmanagementproject.domain.vo.statistical.indicator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工人风险等级人数统计结果视图对象。
 * <p>
 * 统计口径：按工人去重，取每个工人最新一条风险指标记录的风险等级进行分组统计。
 *
 * @author Franz Liszt
 * @since 2026-02-07
 */
@Data
@ApiModel(description = "工人风险等级人数统计结果")
public class RiskLevelCountVO {

    @ApiModelProperty(value = "低风险工人数", example = "50")
    private Integer lowRiskCount;

    @ApiModelProperty(value = "中风险工人数", example = "15")
    private Integer mediumRiskCount;

    @ApiModelProperty(value = "高风险工人数", example = "8")
    private Integer highRiskCount;

    @ApiModelProperty(value = "严重风险工人数", example = "3")
    private Integer veryHighRiskCount;

    @ApiModelProperty(value = "统计总人数（按工人去重）", example = "76")
    private Integer totalCount;
}
