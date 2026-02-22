package gang.lu.riskmanagementproject.common.ai;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 19:55
 */
public interface AiConstants {

    /**
     * 请求字段key
     */
    String MODEL = "model";
    String MAX_TOKENS = "max_tokens";
    String MESSAGES = "messages";
    String ROLE = "role";
    String CONTENT = "content";
    String USER = "user";


    /**
     * 响应字段key
     */
    String CHOICES = "choices";
    String MESSAGE = "message";


    /**
     * 提示词模板
     */
    String PROMPT_HEADER = "你是一个工业安全与职业健康AI分析专家。\n";
    String PROMPT_TABLE_HEADER = "序号 | 记录时间 | 心率(bpm) | 呼吸率(次/分) | 疲劳度(%) | 风险等级 | 是否报警\n";
    String PROMPT_TABLE_DIVIDER = "-----|----------|-----------|--------------|----------|---------|--------\n";
    String PROMPT_ROW_FORMAT = "%d | %s | %d | %d | %.2f | %s | %s\n";
    String PROMPT_UNKNOWN = "未知";
    String PROMPT_ALERT_YES = "是";
    String PROMPT_ALERT_NO = "否";


    /**
     * AI响应JSON字段
     */
    String AI_FIELD_PREDICTED_RISK = "predicted_risk_level";
    String AI_FIELD_RISK_TREND = "risk_trend";
    String AI_FIELD_ANALYSIS = "analysis_summary";
    String AI_FIELD_SUGGESTIONS = "suggestions";
    String AI_FIELD_CONFIDENCE = "confidence_note";
    String AI_DEFAULT_RISK = "未知";
    String AI_DEFAULT_TREND = "平稳";


    /**
     * 匹配 ```json 前缀
     */
    String AI_TEXT_JSON_PATTERN = "```json";
    /**
     * 匹配 ``` 反引号块
     */
    String AI_TEXT_TICK_PATTERN = "```";


    /**
     * http路径
     */
    String PATH_CHAT_COMPLETIONS = "/chat/completions";
}
