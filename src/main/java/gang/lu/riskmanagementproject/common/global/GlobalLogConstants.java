package gang.lu.riskmanagementproject.common.global;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 20:13
 * @description 存储日志信息
 */
public interface GlobalLogConstants {
    /**
     * 全局分页日志
     */
    String LOG_PAGING = "【{}】 分页参数处理：原始【{}, {}】 → 处理后【{}, {}】（规则：默认页大小{}，最大{}）";

    /**
     * 算法记录日志
     */
    String LOG_SEND_REQUEST_TO_ALGORITHM = "【算法服务】 发送视频 【{}】（{} bytes）至算法服务";
    String LOG_ALGORITHM_RETURN_NO_200 = "【算法服务】 算法服务返回非 2xx: {}";
    String LOG_ALGORITHM_RAW_RESPONSE = "【算法服务】 原始响应 ↓\n{}";
    String LOG_FAIL_TO_CONNECT_TO_ALGORITHM = "【算法服务】 连接算法服务失败";

    /**
     * AI记录日志
     */
    String LOG_READY_TO_SEND_TO_AI = "【AI智能分析】 workerId={} 共 {} 条历史数据，准备发送给 AI";
    String LOG_CALL_QWEN_MODEL = "【AI智能分析】 调用 Qwen 模型: {}";
    String LOG_QWEN_NON_2XX = "【AI智能分析】 Qwen API 返回非 2xx: {}";
    String LOG_QWEN_RAW_RESPONSE = "【AI智能分析】 Qwen 原始响应 ↓\n{}";
    String LOG_EXTRACT_CONTENT = "【AI智能分析】 提取 content ↓\n{}";
    String LOG_QWEN_CONNECTION_EXCEPTION = "【AI智能分析】 Qwen API 连接异常";
    String LOG_PARSE_AI_RESPONSE_FAILED = "【AI智能分析】 AI 响应解析失败，原始内容: {}";

    /**
     * 导出pdf日志
     */
    String LOG_GET_HISTORY = "【生成风险报告】 workerId={} 查到 {} 条历史记录";
    String LOG_START_GENERATE_PDF = "【生成风险报告】 开始生成 PDF | workerId={} | includeAi={}";
    String LOG_GENERATE_PDF_DONE = "【生成风险报告】 PDF 生成完成 | 大小: {} KB ({} bytes) | 耗时: {} ms";
    String LOG_GENERATE_PDF_DONE_AND_PRINT = "【生成风险报告】 PDF 生成完成 | 耗时: {} ms";
    String LOG_INVOKE_AI = "【生成风险报告】 调用 AI 预测 | workerId={}";
    String LOG_RESPONSE_COMMITTED = "【生成风险报告】响应是否已提交: {}";
    String LOG_PDF_EXPORT_SUCCESS = "【生成风险报告】PDF导出成功 workerId={}, size={}KB";
    String LOG_PDF_GENERATE_FAILED = "【生成风险报告】PDF生成失败 workerId={}";
    String LOG_FONT_LOAD_SUCCESS = "【生成风险报告】字体加载成功，字体名: {}";

    /**
     * 视频分析日志
     */
    String LOG_VIDEO_SAVE = "【视频算法分析】 已保存 workerId={}, riskLevel={}";
    String LOG_UNKNOWN_RISK_LEVEL = "【视频算法分析】 未知风险等级 '{}', 默认使用 LOW_RISK";
}
