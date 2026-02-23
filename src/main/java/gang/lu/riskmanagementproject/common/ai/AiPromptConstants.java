package gang.lu.riskmanagementproject.common.ai;

/**
 * AI提示词专用常量
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
public interface AiPromptConstants {

    /**
     * 提示词基础模板
     */
    String PROMPT_HEADER = "你是一个工业安全与职业健康AI分析专家。\n";
    String PROMPT_TABLE_HEADER = "序号 | 记录时间 | 心率(bpm) | 呼吸率(次/分) | 疲劳度(%) | 风险等级 | 是否报警\n";
    String PROMPT_TABLE_DIVIDER = "-----|----------|-----------|--------------|----------|---------|--------\n";
    String PROMPT_ROW_FORMAT = "%d | %s | %d | %d | %.2f | %s | %s\n";
    String PROMPT_UNKNOWN = "未知";
    String PROMPT_ALERT_YES = "是";
    String PROMPT_ALERT_NO = "否";

    /**
     * 提示词文本片段
     */
    String PROMPT_DATA_DESC_PREFIX = "以下是工人ID=";
    String PROMPT_DATA_DESC_SUFFIX = " 的历史生理风险监测数据（按时间排序，最新在后）：\n\n";
    String PROMPT_TASK_REQUIRE = "\n请根据以上数据：\n";
    String PROMPT_TASK_1 = "1. 预测该工人未来24小时的风险趋势（仅返回：上升|平稳|下降）\n";
    String PROMPT_TASK_2 = "2. 给出具体、可落地的健康与安全建议（3-5条）\n\n";
    String PROMPT_FORMAT_REQUIRE = "请严格按以下JSON格式返回（不要有任何额外文字、注释、markdown标记）：\n";

    /**
     * 字段约束描述
     */
    String PROMPT_JSON_FIELD_PREDICTED_RISK_DESC = "\"低风险|中风险|高风险|严重风险\"";
    String PROMPT_JSON_FIELD_TREND_DESC = "\"上升|平稳|下降\"";
    String PROMPT_JSON_FIELD_ANALYSIS_DESC = "\"一段简洁的综合分析（50-100字，需包含核心数据趋势）\"";
    String PROMPT_JSON_FIELD_SUGGESTIONS_DESC = "[\"建议1\", \"建议2\", \"建议3\", ...]";
    String PROMPT_JSON_FIELD_CONFIDENCE_DESC = "\"置信度说明（格式：基于XX条数据，预测可信度XX%）\"";


}