package gang.lu.riskmanagementproject.domain.dto.query;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

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
    @Length(max = 100, message = "工号长度不能超过100个字符")
    private String workerCode;

    @ApiModelProperty(value = "姓名（模糊匹配）", example = "张三")
    @Length(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @ApiModelProperty(value = "岗位（模糊匹配）", example = "焊工")
    @Length(max = 100, message = "岗位长度不能超过100个字符")
    private String position;

    @ApiModelProperty(value = "最小工龄", example = "1")
    @PositiveOrZero(message = "最小工龄不能为负数")
    @Max(value = 100, message = "最小工龄不能超过100年")
    private Integer minWorkYears;

    @ApiModelProperty(value = "最大工龄", example = "10")
    @PositiveOrZero(message = "最大工龄不能为负数")
    @Max(value = 100, message = "最大工龄不能超过100年")
    private Integer maxWorkYears;

    @ApiModelProperty(value = "工作类型（枚举值：高空作业/受限空间/设备操作/正常作业）", example = "高空作业")
    @Length(max = 20, message = "工作类型长度不能超过20个字符")
    @ValidEnum(enumClass = WorkType.class, bizName = BusinessConstants.WORK_TYPE)
    private String workTypeValue;


    @ApiModelProperty(value = "状态（枚举值：正常/异常/离线）", example = "正常")
    @Length(max = 10, message = "状态长度不能超过10个字符")
    @ValidEnum(enumClass = Status.class, bizName = BusinessConstants.STATUS)
    private String statusValue;
}