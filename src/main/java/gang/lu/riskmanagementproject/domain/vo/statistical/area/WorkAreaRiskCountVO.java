package gang.lu.riskmanagementproject.domain.vo.statistical.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/7 14:28
 * @description 工作区域风险等级数量统计
 */
@Data
@ApiModel(description = "工作区域风险等级数量统计结果")
public class WorkAreaRiskCountVO {
    @ApiModelProperty(value = "低风险区域数量", example = "10")
    private Integer lowRiskCount;

    @ApiModelProperty(value = "中风险区域数量", example = "5")
    private Integer mediumRiskCount;

    @ApiModelProperty(value = "高风险区域数量", example = "3")
    private Integer highRiskCount;

    @ApiModelProperty(value = "总区域数量", example = "18")
    private Integer totalCount;
}