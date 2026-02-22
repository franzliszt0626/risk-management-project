package gang.lu.riskmanagementproject.domain.vo.statistical.worker;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工人总数统计结果视图对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "工人总数统计结果")
public class WorkerTotalCountVO {

    @ApiModelProperty(value = "工人总数", example = "100")
    private Integer totalCount;

    @ApiModelProperty(value = "统计时间（yyyy-MM-dd HH:mm:ss）", example = "2026-02-09 16:30:00")
    private String statisticTime;
}
