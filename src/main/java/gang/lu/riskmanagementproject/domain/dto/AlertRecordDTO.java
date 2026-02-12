package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:09
 * @description 警报记录数据传输实体
 */
@Data
@ApiModel(description = "预警记录 - 传输对象")
public class AlertRecordDTO {
    @ApiModelProperty(value = "工人id", required = true, example = "1")
    @ValidId(bizName = BusinessConstants.WORKER_ID)
    private Long workerId;

    @ApiModelProperty(value = "预警类型", required = true, example = "心率异常")
    @NotBlank(message = "预警类型不能为空")
    private String alertType;

    @ApiModelProperty(value = "预警级别", required = true, example = "警告")
    @ValidEnum(enumClass = AlertLevel.class, bizName = BusinessConstants.ALERT_LEVEL)
    private String alertLevelValue;

    @ApiModelProperty(value = "信息", example = "工人心率超过120次/分钟")
    @Length(max = 200, message = "预警信息长度不能超过200个字符")
    private String message;

    @ApiModelProperty(value = "是否处理")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人", example = "张三")
    private String handledBy;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}