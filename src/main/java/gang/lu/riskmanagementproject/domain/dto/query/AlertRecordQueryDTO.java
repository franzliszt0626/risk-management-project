package gang.lu.riskmanagementproject.domain.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.domain.enums.field.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.ALERT_LEVEL;
import static gang.lu.riskmanagementproject.common.global.GlobalFormatConstants.DEFAULT_DATE_TIME_FORMAT;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 预警记录多条件分页查询传输对象。
 * <p>
 * 所有字段均为可选过滤条件，不传则不参与查询。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "预警记录 - 多条件分页查询参数")
public class AlertRecordQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "关联工人 ID（精确匹配）", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "预警类型（模糊匹配）", example = "心率异常")
    private String alertType;

    @ApiModelProperty(value = "预警级别（警告 / 严重）", example = "警告")
    @ValidEnum(enumClass = AlertLevel.class, bizName = ALERT_LEVEL)
    private String alertLevelValue;

    @ApiModelProperty(value = "是否已处理（true=已处理 / false=未处理）", example = "false")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人姓名（模糊匹配）", example = "张三")
    private String handledBy;

    @ApiModelProperty(value = "创建时间 - 起始（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 00:00:00")
    @PastOrPresent(message = ALERT_RECORD_START_TIME_INVALID)
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime createdStartTime;

    @ApiModelProperty(value = "创建时间 - 截止（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-28 23:59:59")
    @PastOrPresent(message = ALERT_RECORD_END_TIME_INVALID)
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime createdEndTime;

    @ApiModelProperty(value = "处理时间 - 起始（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 00:00:00")
    @PastOrPresent(message = ALERT_RECORD_HANDLE_START_TIME_INVALID)
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime handleStartTime;

    @ApiModelProperty(value = "处理时间 - 截止（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-28 23:59:59")
    @PastOrPresent(message = ALERT_RECORD_HANDLE_END_TIME_INVALID)
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    private LocalDateTime handleEndTime;
}