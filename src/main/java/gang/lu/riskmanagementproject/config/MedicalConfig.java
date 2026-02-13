package gang.lu.riskmanagementproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/13 14:41
 * @description 生理参数配置
 */
@Configuration
public class MedicalConfig {

    public static final Integer DEFAULT_MIN_HEART_RATE = 1;
    public static final Integer DEFAULT_MAX_HEART_RATE = 300;
    public static final Integer DEFAULT_MIN_RESPIRATORY_RATE = 1;
    public static final Integer DEFAULT_MAX_RESPIRATORY_RATE = 60;
    public static final Integer DEFAULT_MIN_FATIGUE_PERCENT = 0;
    public static final Integer DEFAULT_MAX_FATIGUE_PERCENT = 100;

    public static Double MAX_FATIGUE_PERCENT;

    public static Double MIN_FATIGUE_PERCENT;

    public static Integer MAX_HEART_RATE;

    public static Integer MIN_HEART_RATE;

    public static Integer MAX_RESPIRATORY_RATE;

    public static Integer MIN_RESPIRATORY_RATE;

    @Value("${spring.medical.max-fatigue-percent}")
    public static void setMaxFatiguePercent(Double maxFatiguePercent) {
        MAX_FATIGUE_PERCENT = maxFatiguePercent;
    }

    @Value("${spring.medical.min-fatigue-percent}")
    public static void setMinFatiguePercent(Double minFatiguePercent) {
        MIN_FATIGUE_PERCENT = minFatiguePercent;
    }

    @Value("${spring.medical.max-heart-rate}")
    public static void setMaxHeartRate(Integer maxHeartRate) {
        MAX_HEART_RATE = maxHeartRate;
    }

    @Value("${spring.medical.min-heart-rate}")
    public static void setMinHeartRate(Integer minHeartRate) {
        MIN_HEART_RATE = minHeartRate;
    }

    @Value("${spring.medical.max-respiratory-rate}")
    public static void setMaxRespiratoryRate(Integer maxRespiratoryRate) {
        MAX_RESPIRATORY_RATE = maxRespiratoryRate;
    }

    @Value("${spring.medical.min-respiratory-rate}")
    public static void setMinRespiratoryRate(Integer minRespiratoryRate) {
        MIN_RESPIRATORY_RATE = minRespiratoryRate;
    }
}
