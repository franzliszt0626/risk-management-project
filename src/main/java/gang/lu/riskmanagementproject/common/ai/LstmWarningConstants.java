package gang.lu.riskmanagementproject.common.ai;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/27 13:28
 * @description LSTM的警告词
 */
public interface LstmWarningConstants {
    String FATIGUE_OVER_HIGH_THRESHOLD = "预测疲劳峰值达 %.1f%%，已超过严重阈值（%.0f%%），请立即安排休息或人工干预！";
    String FATIGUE_OVER_WARN_THRESHOLD = "预测疲劳峰值达 %.1f%%，已超过预警阈值（%.0f%%），建议安排休息！";
    String FATIGUE_NORMAL              = "预测疲劳峰值为 %.1f%%，当前状态良好，请继续保持监控！";
}
