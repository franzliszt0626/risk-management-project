package gang.lu.riskmanagementproject.domain.dto;


import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

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
    @NotBlank(message = "工号不能为空")
    @Length(max = 100, message = "工号长度不能超过100个字符")
    private String workerCode;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @ApiModelProperty(value = "岗位", example = "焊工")
    @Length(max = 100, message = "岗位长度不能超过100个字符")
    private String position;

    @ApiModelProperty(value = "工龄（年）", example = "8")
    @PositiveOrZero(message = "工龄不能为负数")
    @Max(value = 100, message = "工龄不能超过100年")
    private Integer workYears;

    @ApiModelProperty(value = "工作种类", example = "高空作业")
    private WorkType workType;

    @ApiModelProperty(value = "当前状态", example = "正常")
    private Status status;
}