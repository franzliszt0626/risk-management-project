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
    String LOG_READY_TO_SEND_TO_AI = "【AI分析】 workerId={} 共 {} 条历史数据，准备发送给 AI";

    /**
     * 导出pdf日志
     */
    String LOG_GET_HISTORY = "【风险报告】 workerId={} 查到 {} 条历史记录";
    String LOG_START_GENERATE_PDF = "【风险报告】 开始生成 PDF | workerId={} | includeAi={}";
    String LOG_GENERATE_PDF_DONE = "【风险报告】 PDF 生成完成 | 大小: {} KB ({} bytes) | 耗时: {} ms";
    String LOG_GENERATE_PDF_DONE_AND_PRINT = "【风险报告】 PDF 生成完成 | 耗时: {} ms";
    String LOG_INVOKE_AI = "【风险报告】 调用 AI 预测 | workerId={}";

    /**
     * 视频分析日志
     */
    String LOG_VIDEO_SAVE = "【视频分析】 已保存 workerId={}, riskLevel={}";
    String LOG_UNKNOWN_RISK_LEVEL = "【视频分析】 未知风险等级 '{}', 默认使用 LOW_RISK";
}
