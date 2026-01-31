package gang.lu.riskmanagementproject.domain.vo;

import io.swagger.annotations.ApiModel;
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
    private Long workerId;
    private String alertType;
    private String alertLevel;
    private String message;
    private Boolean isHandled;
    private String handledBy;
    private LocalDateTime handleTime;
    private LocalDateTime createdTime;
}