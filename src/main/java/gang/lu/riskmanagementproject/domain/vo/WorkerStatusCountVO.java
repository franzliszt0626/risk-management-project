package gang.lu.riskmanagementproject.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 15:06
 * @description 工人状态数量统计VO
 */
@Data
@ApiModel(description = "工人状态数量统计结果")
public class WorkerStatusCountVO {
    @ApiModelProperty(value = "正常状态工人数量", example = "50")
    private Integer normalCount;

    @ApiModelProperty(value = "异常状态工人数量", example = "5")
    private Integer abnormalCount;

    @ApiModelProperty(value = "离线状态工人数量", example = "3")
    private Integer offlineCount;

    @ApiModelProperty(value = "工人总数量", example = "58")
    private Integer totalCount;
}
