package gang.lu.riskmanagementproject.domain.dto;


import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:54
 * @description 工人数据传输
 */
@Data
@ApiModel(description = "工人信息 - 传输对象")
public class WorkerDTO {
    @ApiModelProperty(value = "工号", example = "W1001")
    private String workerCode;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String name;

    @ApiModelProperty(value = "岗位", example = "焊工")
    private String position;

    @ApiModelProperty(value = "工龄（年）", example = "8")
    private Integer workYears;

    @ApiModelProperty(value = "工作种类")
    private WorkType workType;

    @ApiModelProperty(value = "当前状态")
    private Status status;
}