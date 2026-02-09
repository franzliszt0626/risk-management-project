package gang.lu.riskmanagementproject.domain.vo;

import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:10
 * @description 警告报告前端展示
 */


@Data
@ApiModel(description = "预警记录 - 视图对象")
public class AlertRecordVO {

    @ApiModelProperty("预警类型")
    private String alertType;

    @ApiModelProperty(value = "预警等级", allowableValues = "严重、警告")
    private AlertLevel alertLevel;

    @ApiModelProperty("预警描述")
    private String message;

    @ApiModelProperty("是否处理")
    private Boolean isHandled;

    @ApiModelProperty("处理人")
    private String handledBy;

    @ApiModelProperty("处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdTime;
}