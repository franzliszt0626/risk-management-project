package gang.lu.riskmanagementproject.common.ai;

/**
 * AI日志专用常量
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
public interface AiLogConstants {

    /**
     * AI调用日志模板
     */
    String LOG_CALL_QWEN_MODEL = "[AiHelper] 调用 Qwen 模型: {}";
    String LOG_QWEN_NON_2XX = "[AiHelper] Qwen API 返回非 2xx: {}";
    String LOG_QWEN_RAW_RESPONSE = "[AiHelper] Qwen 原始响应 ↓\n{}";
    String LOG_EXTRACT_CONTENT = "[AiHelper] 提取 content ↓\n{}";
    String LOG_QWEN_CONNECTION_EXCEPTION = "[AiHelper] Qwen API 连接异常";
    String LOG_PARSE_AI_RESPONSE_FAILED = "[AiHelper] AI 响应解析失败，原始内容: {}";
}