package gang.lu.riskmanagementproject.domain.dto.query;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.STATUS;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.WORK_TYPE;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_YEAR_INVALID;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_YEAR_NEGATIVE_INVALID;

/**
 * 工人信息多条件分页查询传输对象。
 * <p>
 * 所有字段均为可选过滤条件，不传则不参与查询。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "工人信息 - 多条件分页查询参数")
public class WorkerQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "工号（模糊匹配）", example = "W001")
    private String workerCode;

    @ApiModelProperty(value = "姓名（模糊匹配）", example = "张三")
    private String name;

    @ApiModelProperty(value = "岗位名称（模糊匹配）", example = "焊工")
    private String position;

    @ApiModelProperty(value = "工龄最小值（年，≥ 0）", example = "1")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    private Integer minWorkYears;

    @ApiModelProperty(value = "工龄最大值（年，≤ 100）", example = "10")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    @Max(value = 100, message = WORKER_YEAR_INVALID)
    private Integer maxWorkYears;

    @ApiModelProperty(value = "工种（高空作业 / 受限空间 / 设备操作 / 正常作业）", example = "高空作业")
    @ValidEnum(enumClass = WorkType.class, bizName = WORK_TYPE)
    private String workTypeValue;

    @ApiModelProperty(value = "当前状态（正常 / 异常 / 离线）", example = "正常")
    @ValidEnum(enumClass = Status.class, bizName = STATUS)
    private String statusValue;
}