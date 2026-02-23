package gang.lu.riskmanagementproject.common.ai;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 14:47
 * @description AI响应字段
 */
public interface AiFieldConstants {

    /**
     * 请求字段名
     */
    String MODEL = "model";
    String MAX_TOKENS = "max_tokens";
    String MESSAGES = "messages";
    String ROLE = "role";
    String CONTENT = "content";
    String USER = "user";

    /**
     * 响应字段名
     */
    String CHOICES = "choices";
    String MESSAGE = "message";
    String AI_FIELD_PREDICTED_RISK = "predicted_risk_level";
    String AI_FIELD_RISK_TREND = "risk_trend";
    String AI_FIELD_ANALYSIS = "analysis_summary";
    String AI_FIELD_SUGGESTIONS = "suggestions";
    String AI_FIELD_CONFIDENCE = "confidence_note";
    String AI_DEFAULT_RISK = "未知";
    String AI_DEFAULT_TREND = "平稳";
}
