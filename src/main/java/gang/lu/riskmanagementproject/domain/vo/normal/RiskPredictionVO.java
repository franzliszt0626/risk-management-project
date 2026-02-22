package gang.lu.riskmanagementproject.domain.vo.normal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:20
 * @description 模型预测返回前端的视图
 */
@Data
@ApiModel(description = "AI风险预测结果")
public class RiskPredictionVO {

    @ApiModelProperty("工人ID")
    private Long workerId;

    @ApiModelProperty("分析的历史记录条数")
    private Integer recordCount;

    @ApiModelProperty("AI预测的未来风险等级（低风险/中风险/高风险/严重风险）")
    private String predictedRiskLevel;

    @ApiModelProperty("风险趋势（上升/平稳/下降）")
    private String riskTrend;

    @ApiModelProperty("AI分析摘要")
    private String analysisSummary;

    @ApiModelProperty("具体建议列表")
    private List<String> suggestions;

    @ApiModelProperty("置信度描述")
    private String confidenceNote;
}