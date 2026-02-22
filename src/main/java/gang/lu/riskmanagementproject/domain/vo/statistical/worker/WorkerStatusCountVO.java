package gang.lu.riskmanagementproject.domain.vo.statistical.worker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工人在线状态数量统计结果视图对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "工人状态数量统计结果")
public class WorkerStatusCountVO {

    @ApiModelProperty(value = "正常状态工人数", example = "50")
    private Integer normalCount;

    @ApiModelProperty(value = "异常状态工人数", example = "5")
    private Integer abnormalCount;

    @ApiModelProperty(value = "离线状态工人数", example = "3")
    private Integer offlineCount;

    @ApiModelProperty(value = "工人总数", example = "58")
    private Integer totalCount;
}