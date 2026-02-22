package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 工人信息新增 / 修改传输对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "工人信息 - 传输对象")
public class WorkerDTO {

    @ApiModelProperty(value = "工号（全局唯一，最多 100 字符）", required = true, example = "W1001")
    @NotBlank(message = WORKER_CODE_EMPTY)
    @Length(max = 100, message = WORKER_CODE_INVALID)
    private String workerCode;

    @ApiModelProperty(value = "姓名（最多 50 字符）", required = true, example = "张三")
    @NotBlank(message = WORKER_NAME_EMPTY)
    @Length(max = 50, message = WORKER_NAME_INVALID)
    private String name;

    @ApiModelProperty(value = "岗位名称（最多 100 字符）", example = "焊工")
    @Length(max = 100, message = WORKER_POSITION_INVALID)
    private String position;

    @ApiModelProperty(value = "工龄（年，0-100）", example = "8")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    @Max(value = 100, message = WORKER_YEAR_INVALID)
    private Integer workYears;

    @ApiModelProperty(value = "工种（高空作业 / 受限空间 / 设备操作 / 正常作业）", required = true, example = "高空作业")
    @NotBlank(message = WORKER_TYPE_EMPTY)
    @ValidEnum(enumClass = WorkType.class, bizName = BusinessConstants.WORK_TYPE)
    private String workTypeValue;

    @ApiModelProperty(value = "当前状态（正常 / 异常 / 离线）", required = true, example = "正常")
    @NotBlank(message = WORKER_STATUS_EMPTY)
    @ValidEnum(enumClass = Status.class, bizName = BusinessConstants.STATUS)
    private String statusValue;
}