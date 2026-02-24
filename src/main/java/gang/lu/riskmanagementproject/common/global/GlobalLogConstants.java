package gang.lu.riskmanagementproject.common.global;

/**
 * 全局日志消息常量
 * <p>
 * 统一管理各模块日志输出的消息模板，格式为：【日志前缀】日志描述。
 * 含 {@code {}} 占位符的常量需配合 SLF4J 参数化日志使用。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>日志前缀常量</li>
 *   <li>全局分页日志</li>
 *   <li>算法服务日志</li>
 *   <li>AI 智能分析日志</li>
 *   <li>导出 PDF 日志</li>
 *   <li>视频算法分析日志</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface GlobalLogConstants {


    // ==============================1. 全局分页日志================================

    String LOG_PAGING = "【{}】 分页参数处理：原始【{}, {}】 → 处理后【{}, {}】（规则：默认页大小{}，最大{}）！";

    // ==============================2. 算法服务日志================================

    String LOG_ALGORITHM_SEND_REQUEST    = "【算法服务】发送视频 【{}】（{} bytes）至算法服务！";
    String LOG_ALGORITHM_NON_2XX         = "【算法服务】算法服务返回非 2xx: {}！";
    String LOG_ALGORITHM_RAW_RESPONSE    = "【算法服务】原始响应 ↓\n{}";
    String LOG_ALGORITHM_CONNECTION_FAIL = "【算法服务】连接算法服务失败！";

    // ==============================3. AI 智能分析日志================================

    String LOG_AI_READY_TO_SEND             = "【AI智能分析】workerId={} 共 {} 条历史数据，准备发送给 AI！";
    String LOG_AI_CALL_QWEN_MODEL           = "【AI智能分析】调用 Qwen 模型: {}！";
    String LOG_AI_QWEN_NON_2XX              = "【AI智能分析】Qwen API 返回非 2xx: {}！";
    String LOG_AI_QWEN_RAW_RESPONSE         = "【AI智能分析】Qwen 原始响应 ↓\n{}";
    String LOG_AI_EXTRACT_CONTENT           = "【AI智能分析】提取 content ↓\n{}";
    String LOG_AI_QWEN_CONNECTION_EXCEPTION = "【AI智能分析】Qwen API 连接异常！";
    String LOG_AI_PARSE_RESPONSE_FAILED     = "【AI智能分析】AI 响应解析失败，原始内容: {}！";

    // ==============================4. 导出 PDF 日志================================

    String LOG_PDF_GET_HISTORY          = "【生成风险报告】workerId={} 查到 {} 条历史记录！";
    String LOG_PDF_START_GENERATE       = "【生成风险报告】开始生成 PDF | workerId={} | includeAi={}！";
    String LOG_PDF_GENERATE_DONE        = "【生成风险报告】PDF 生成完成 | 大小: {} KB ({} bytes) | 耗时: {} ms！";
    String LOG_PDF_GENERATE_DONE_SIMPLE = "【生成风险报告】PDF 生成完成 | 耗时: {} ms！";
    String LOG_PDF_INVOKE_AI            = "【生成风险报告】调用 AI 预测 | workerId={}！";
    String LOG_PDF_RESPONSE_COMMITTED   = "【生成风险报告】响应是否已提交: {}！";
    String LOG_PDF_EXPORT_SUCCESS       = "【生成风险报告】PDF 导出成功 | workerId={}, size={} KB！";
    String LOG_PDF_GENERATE_FAILED      = "【生成风险报告】PDF 生成失败 | workerId={}！";
    String LOG_PDF_FONT_LOAD_SUCCESS    = "【生成风险报告】字体加载成功，字体名: {}！";

    // ==============================5. 视频算法分析日志================================

    String LOG_VIDEO_SAVE               = "【视频算法分析】已保存 | workerId={}, riskLevel={}！";
    String LOG_VIDEO_UNKNOWN_RISK_LEVEL = "【视频算法分析】未知风险等级 '{}'，默认使用 LOW_RISK！";
}