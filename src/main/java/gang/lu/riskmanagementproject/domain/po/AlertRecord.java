package gang.lu.riskmanagementproject.domain.po;



import com.baomidou.mybatisplus.annotation.*;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:30
 * @description 警报报告
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_alert_record")
public class AlertRecord extends BasePO {

    private Long workerId;

    private String alertType;

    private AlertLevel alertLevel;

    private String message;

    private Boolean isHandled;

    private String handledBy;

    private LocalDateTime handleTime;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime createdTime;
}