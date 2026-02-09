package gang.lu.riskmanagementproject.domain.vo.statistical.worker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 15:06
 * @description 工人工种数量统计VO
 */
@Data
@ApiModel(description = "工人工种数量统计结果")
public class WorkerTypeCountVO {
    @ApiModelProperty(value = "高空作业工人数量", example = "50")
    private Integer highAltitudeCount;

    @ApiModelProperty(value = "受限空间工人数量", example = "5")
    private Integer confinedSpaceCount;

    @ApiModelProperty(value = "设备操作工人数量", example = "3")
    private Integer equipmentOperationCount;

    @ApiModelProperty(value = "正常作业工人数量", example = "50")
    private Integer normalWorkCount;

    @ApiModelProperty(value = "工人总数量", example = "58")
    private Integer totalCount;
}
