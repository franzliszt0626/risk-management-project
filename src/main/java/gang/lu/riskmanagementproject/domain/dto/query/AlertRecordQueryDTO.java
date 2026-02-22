package gang.lu.riskmanagementproject.domain.dto.query;


import com.fasterxml.jackson.annotation.JsonFormat;
import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 15:12
 * @description 预警记录组合查询DTO（含分页+多条件筛选）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "预警记录组合查询DTO（含分页参数）")
public class AlertRecordQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "工人ID（精确匹配）", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "预警类型（模糊查询）", example = "心率异常")
    private String alertType;

    @ApiModelProperty(value = "预警级别（警告/严重）", example = "警告")
    @ValidEnum(enumClass = AlertLevel.class, bizName = BusinessConstants.ALERT_LEVEL)
    private String alertLevelValue;

    @ApiModelProperty(value = "是否已处理（true=已处理/false=未处理）", example = "false")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人（模糊查询）", example = "张三")
    private String handledBy;

    @ApiModelProperty(value = "创建时间开始（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 00:00:00")
    @PastOrPresent(message = ALERT_RECORD_START_TIME_INVALID)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdStartTime;

    @ApiModelProperty(value = "创建时间结束（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-02 23:59:59")
    @PastOrPresent(message = ALERT_RECORD_END_TIME_INVALID)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdEndTime;

    @ApiModelProperty(value = "处理时间开始（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-01 00:00:00")
    @PastOrPresent(message = ALERT_RECORD_HANDLE_START_TIME_INVALID)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime handleStartTime;

    @ApiModelProperty(value = "处理时间结束（格式：yyyy-MM-dd HH:mm:ss）", example = "2026-02-02 23:59:59")
    @PastOrPresent(message = ALERT_RECORD_HANDLE_END_TIME_INVALID)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime handleEndTime;
}