package gang.lu.riskmanagementproject.domain.dto;


import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static gang.lu.riskmanagementproject.common.FailedMessages.*;

/**
 * @author Franz Liszt
 * @date 2026-02-09 21:07:12
 * @description 工人模型传输对象
 * @version 1.0
 */
@Data
@ApiModel(description = "工人信息 - 传输对象")
public class WorkerDTO {
    @ApiModelProperty(value = "工号", required = true, example = "W1001")
    @Length(max = 100, message = WORKER_CODE_INVALID)
    private String workerCode;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    @Length(max = 50, message = WORKER_NAME_INVALID)
    private String name;

    @ApiModelProperty(value = "岗位", example = "焊工")
    @Length(max = 100, message = WORKER_POSITION_INVALID)
    private String position;

    @ApiModelProperty(value = "工龄（年）", example = "8")
    @PositiveOrZero(message = WORKER_YEAR_NEGATIVE_INVALID)
    @Max(value = 100, message = WORKER_YEAR_INVALID)
    private Integer workYears;

    @ApiModelProperty(value = "工作种类", example = "高空作业")
    @ValidEnum(enumClass = WorkType.class, bizName = BusinessConstants.WORK_TYPE)
    private String workTypeValue;

    @ApiModelProperty(value = "当前状态", example = "正常")
    @ValidEnum(enumClass = Status.class, bizName = BusinessConstants.STATUS)
    private String statusValue;
}