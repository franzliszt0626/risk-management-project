package gang.lu.riskmanagementproject.config;

import lombok.Data;
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
public class MedicalConfig {
    private Double minFatiguePercent = 0.0;
    private Double maxFatiguePercent = 100.0;
    private Integer minHeartRate = 1;
    private Integer maxHeartRate = 300;
    private Integer minRespiratoryRate = 1;
    private Integer maxRespiratoryRate = 60;
}
