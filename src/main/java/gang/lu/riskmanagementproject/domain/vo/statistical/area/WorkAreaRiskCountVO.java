package gang.lu.riskmanagementproject.domain.vo.statistical.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作区域风险等级数量统计结果视图对象。
 *
 * @author Franz Liszt
 * @since 2026-02-07
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

    @ApiModelProperty(value = "区域总数", example = "18")
    private Integer totalCount;
}