package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "工人id")
    private Long workerId;

    @ApiModelProperty(value = "预警类型")
    private String alertType;

    @ApiModelProperty(value = "预警级别")
    private AlertLevel alertLevel;

    @ApiModelProperty(value = "信息")
    private String message;

    @ApiModelProperty(value = "是否处理")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人")
    private String handledBy;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}