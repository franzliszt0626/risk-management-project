package gang.lu.riskmanagementproject.domain.vo.statistical.alert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 20:45
 * @description 统计未处理的预警记录个数
 */
@Data
@ApiModel(description = "未处理预警记录统计结果")
public class AlertUnhandledCountVO {

    @ApiModelProperty(value = "警告级别未处理数", example = "12")
    private Integer warningCount;

    @ApiModelProperty(value = "严重级别未处理数", example = "5")
    private Integer criticalCount;

    @ApiModelProperty(value = "未处理总数", example = "17")
    private Integer totalUnhandledCount;
}
