package gang.lu.riskmanagementproject.domain.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:28
 * @description 风险历史
 */
@Data
@Accessors(chain = true)
@TableName("t_risk_history")
public class RiskHistory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long workerId;

    private Integer heartRate;

    private Double sdnnMs;

    private Double fatiguePercent;

    private Integer breathRate;

    private Integer auScore;

    private Integer stressLevel;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime recordTime;
}