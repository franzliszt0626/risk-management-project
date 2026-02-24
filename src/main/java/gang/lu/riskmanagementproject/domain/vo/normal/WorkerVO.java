package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工人信息视图对象，用于前端展示。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@ApiModel(description = "工人信息 - 视图对象")
public class WorkerVO {

    @ApiModelProperty(value = "工人id", example = "1")
    private Long id;

    @ApiModelProperty(value = "工号", example = "W1001")
    private String workerCode;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String name;

    @ApiModelProperty(value = "岗位名称", example = "焊工")
    private String position;

    @ApiModelProperty(value = "工龄（年）", example = "8")
    private Integer workYears;

    @ApiModelProperty(value = "工种（高空作业 / 受限空间 / 设备操作 / 正常作业）",
            allowableValues = "高空作业,受限空间,设备操作,正常作业", example = "高空作业")
    private WorkType workType;

    @ApiModelProperty(value = "当前状态（正常 / 异常 / 离线）",
            allowableValues = "正常,异常,离线", example = "正常")
    private Status status;
}