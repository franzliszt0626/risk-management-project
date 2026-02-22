package gang.lu.riskmanagementproject.domain.vo.normal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * AI 风险预测结果视图对象。
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
@Data
@ApiModel(description = "AI 风险预测结果 - 视图对象")
public class RiskPredictionVO {

    @ApiModelProperty(value = "关联工人 ID", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "参与分析的历史记录条数", example = "20")
    private Integer recordCount;

    @ApiModelProperty(value = "AI 预测的未来风险等级（低风险 / 中风险 / 高风险 / 严重风险）",
            allowableValues = "低风险,中风险,高风险,严重风险", example = "高风险")
    private String predictedRiskLevel;

    @ApiModelProperty(value = "风险趋势（上升 / 平稳 / 下降）",
            allowableValues = "上升,平稳,下降", example = "上升")
    private String riskTrend;

    @ApiModelProperty(value = "AI 综合分析摘要（100 字以内）", example = "该工人心率持续偏高，疲劳指数上升，建议安排休息。")
    private String analysisSummary;

    @ApiModelProperty(value = "具体健康与安全建议列表")
    private List<String> suggestions;

    @ApiModelProperty(value = "预测置信度说明", example = "基于 20 条历史数据，置信度较高。")
    private String confidenceNote;
}