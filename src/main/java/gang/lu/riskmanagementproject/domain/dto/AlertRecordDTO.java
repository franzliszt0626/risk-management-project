package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import io.swagger.annotations.ApiModel;
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
    private Long workerId;
    private String alertType;
    private AlertLevel alertLevel;
    private String message;
    private Boolean isHandled;
    private String handledBy;
    private LocalDateTime handleTime;
    private LocalDateTime createdTime;
}