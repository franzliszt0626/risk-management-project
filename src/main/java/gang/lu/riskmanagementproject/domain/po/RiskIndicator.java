package gang.lu.riskmanagementproject.domain.po;


import com.baomidou.mybatisplus.annotation.*;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:19
 * @description 风险指标类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_risk_indicator")
public class RiskIndicator extends BasePO {


    /**
     * 对应的工人表中的id
     */
    private Long workerId;

    /**
     * 心率
     */
    private Integer heartRate;

    /**
     * 呼吸率
     */
    private Integer respiratoryRate;

    /**
     * 劳累百分比，越高风险越大
     */
    private Double fatiguePercent;

    /**
     * 风险等级
     */
    private RiskLevel riskLevel;

    /**
     * 是否警告
     */
    private Boolean alertFlag;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime recordTime;
}