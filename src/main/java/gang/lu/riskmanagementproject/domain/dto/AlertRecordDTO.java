package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 预警记录新增 / 修改传输对象。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@ApiModel(description = "预警记录 - 传输对象")
public class AlertRecordDTO {

    @ApiModelProperty(value = "关联工人 ID", required = true, example = "1")
    @NotNull(message = WORKER_NOT_EXIST)
    @ValidId(bizName = BusinessConstants.WORKER_ID)
    private Long workerId;

    @ApiModelProperty(value = "预警类型（如：心率异常、疲劳超标）", required = true, example = "心率异常")
    @NotBlank(message = ALERT_TYPE_EMPTY)
    private String alertType;

    @ApiModelProperty(value = "预警级别（警告 / 严重）", required = true, example = "警告")
    @NotBlank(message = ALERT_LEVEL_EMPTY)
    @ValidEnum(enumClass = AlertLevel.class, bizName = BusinessConstants.ALERT_LEVEL)
    private String alertLevelValue;

    @ApiModelProperty(value = "预警消息内容", example = "工人心率超过 120 次/分钟")
    @Length(max = 200, message = ALERT_RECORD_MESSAGE_INVALID)
    private String message;

    @ApiModelProperty(value = "是否已处理（默认 false）", example = "false")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人姓名", example = "张三")
    private String handledBy;

    @ApiModelProperty(value = "处理时间（格式：yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime handleTime;
}