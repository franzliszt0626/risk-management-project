package gang.lu.riskmanagementproject.domain.vo.normal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LSTM 疲劳预测结果视图对象。
 * <p>
 * 基于工人历史生理指标（心率 / 呼吸率 / 疲劳百分比）进行时序建模，
 * 返回未来 6 次疲劳百分比预测值及风险提示。
 *
 * @author Franz Liszt
 * @since 2026-02-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "LSTM 疲劳预测结果 - 视图对象")
public class FatiguePredictionVO {

    @ApiModelProperty(value = "关联工人 ID", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "参与训练的有效历史记录条数", example = "30")
    private Integer historyCount;

    @ApiModelProperty(value = "未来 6 次疲劳百分比预测值（%，精确到小数点后两位）",
            example = "[62.5, 65.3, 68.1, 71.0, 73.4, 76.2]")
    private List<Double> predictedFatigueList;

    @ApiModelProperty(value = "整体疲劳趋势（上升 / 平稳 / 下降）",
            allowableValues = "上升,平稳,下降", example = "上升")
    private String trend;

    @ApiModelProperty(value = "根据预测峰值自动生成的风险提示",
            example = "预测疲劳峰值达 76.2%，已超过预警阈值，建议安排休息。")
    private String riskTip;
}