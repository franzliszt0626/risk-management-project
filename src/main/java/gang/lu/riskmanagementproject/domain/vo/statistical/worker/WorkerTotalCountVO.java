package gang.lu.riskmanagementproject.domain.vo.statistical.worker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 16:26
 * @description 工人总数统计
 */
@Data
@ApiModel(description = "工人总数统计结果VO")
public class WorkerTotalCountVO {
    @ApiModelProperty(value = "工人总数量", example = "100")
    private Integer totalCount;

    @ApiModelProperty(value = "统计时间", example = "2026-02-09 16:30:00")
    private String statisticTime;
}