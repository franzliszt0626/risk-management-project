package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import gang.lu.riskmanagementproject.domain.enums.field.AlertLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 预警记录实体，对应数据库表 {@code t_alert_record}。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_alert_record")
public class AlertRecord extends BasePO {

    /**
     * 关联工人 ID
     */
    private Long workerId;

    /**
     * 预警类型（如：心率异常、疲劳超标）
     */
    private String alertType;

    /**
     * 预警级别
     */
    private AlertLevel alertLevel;

    /**
     * 预警消息内容
     */
    private String message;

    /**
     * 是否已处理
     */
    private Boolean isHandled;

    /**
     * 处理人姓名
     */
    private String handledBy;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
}