package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.field.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预警记录视图对象，用于前端展示。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@ApiModel(description = "预警记录 - 视图对象")
public class AlertRecordVO {

    @ApiModelProperty(value = "关联工人 ID", example = "1")
    private Long workerId;

    @ApiModelProperty(value = "预警类型（如：心率异常、疲劳超标）", example = "心率异常")
    private String alertType;

    @ApiModelProperty(value = "预警级别（警告 / 严重）", allowableValues = "警告,严重", example = "警告")
    private AlertLevel alertLevel;

    @ApiModelProperty(value = "预警消息内容", example = "工人心率超过 120 次/分钟")
    private String message;

    @ApiModelProperty(value = "是否已处理", example = "false")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人姓名", example = "张三")
    private String handledBy;

    @ApiModelProperty(value = "处理时间", example = "2026-02-01 10:30:00")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "记录创建时间", example = "2026-02-01 09:00:00")
    private LocalDateTime createTime;
}