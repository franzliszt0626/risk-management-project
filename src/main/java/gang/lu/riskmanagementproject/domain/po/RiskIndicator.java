package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 风险指标实体，对应数据库表 {@code t_risk_indicator}。
 * <p>
 * 记录工人某时刻的生理监测数据，由视频分析算法写入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_risk_indicator")
public class RiskIndicator extends BasePO {

    /**
     * 关联工人 ID
     */
    private Long workerId;

    /**
     * 心率（bpm）
     */
    private Integer heartRate;

    /**
     * 呼吸率（次/min）
     */
    private Integer respiratoryRate;

    /**
     * 疲劳百分比（%），值越高风险越大
     */
    private Double fatiguePercent;

    /**
     * 综合风险等级
     */
    private RiskLevel riskLevel;

    /**
     * 是否触发报警
     */
    private Boolean alertFlag;
}