package gang.lu.riskmanagementproject.domain.dto.query;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.PositiveOrZero;

import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_YEAR_NEGATIVE_INVALID;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-09 21:08:20
 * @description 工人组合条件查询DTO（分页）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "工人组合查询条件DTO")
public class WorkerQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "工号（模糊匹配）", example = "W001")
    private String workerCode;

    @ApiModelProperty(value = "姓名（模糊匹配）", example = "张三")
    private String name;

    @ApiModelProperty(value = "岗位（模糊匹配）", example = "焊工")
    private String position;

    @ApiModelProperty(value = "最小工龄", example = "1")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    private Integer minWorkYears;

    @ApiModelProperty(value = "最大工龄", example = "10")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    private Integer maxWorkYears;

    @ApiModelProperty(value = "工作类型（枚举值：高空作业/受限空间/设备操作/正常作业）", example = "高空作业")
    @ValidEnum(enumClass = WorkType.class, bizName = BusinessConstants.WORK_TYPE)
    private String workTypeValue;

    @ApiModelProperty(value = "状态（枚举值：正常/异常/离线）", example = "正常")
    @ValidEnum(enumClass = Status.class, bizName = BusinessConstants.STATUS)
    private String statusValue;
}