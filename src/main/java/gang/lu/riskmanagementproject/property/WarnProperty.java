package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/27 11:47
 * @description 风险警告阈值
 */
@Data
@Component
public class WarnProperty {

    /**
     * 疲劳值超过此阈值触发警告提示
     */
    @Value("${warn.fatigue-warn-threshold}")
    private double warnThreshold;

    /**
     * 疲劳值超过此阈值触发严重提示
     */
    @Value("${warn.fatigue-high-threshold}")
    private double highThreshold;

    /**
     * 平稳指标
     */
    @Value("${warn.steady}")
    private double steady;

    /**
     * 上升指标
     */
    @Value("${warn.upward}")
    private double upward;

    /**
     * 下降指标
     */
    @Value("${warn.downward}")
    private double downward;

}
