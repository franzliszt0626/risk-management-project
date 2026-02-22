package gang.lu.riskmanagementproject.domain.vo.statistical.worker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工人工种数量统计结果视图对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "工人工种数量统计结果")
public class WorkerTypeCountVO {

    @ApiModelProperty(value = "高空作业工人数", example = "20")
    private Integer highAltitudeCount;

    @ApiModelProperty(value = "受限空间工人数", example = "15")
    private Integer confinedSpaceCount;

    @ApiModelProperty(value = "设备操作工人数", example = "13")
    private Integer equipmentOperationCount;

    @ApiModelProperty(value = "正常作业工人数", example = "10")
    private Integer normalWorkCount;

    @ApiModelProperty(value = "工人总数", example = "58")
    private Integer totalCount;
}