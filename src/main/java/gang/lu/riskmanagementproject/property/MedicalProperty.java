package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/13 14:41
 * @description 生理参数配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "medical")
public class MedicalProperty {

    @Value("${medical.min-fatigue-percent:0.0}")
    private Double minFatiguePercent;

    @Value("${medical.max-fatigue-percent:100.0}")
    private Double maxFatiguePercent;

    @Value("${medical.min-heart-rate:1}")
    private Integer minHeartRate;

    @Value("${medical.max-heart-rate:300}")
    private Integer maxHeartRate;

    @Value("${medical.min-respiratory-rate:1}")
    private Integer minRespiratoryRate;

    @Value("${medical.max-respiratory-rate:60}")
    private Integer maxRespiratoryRate;
}
