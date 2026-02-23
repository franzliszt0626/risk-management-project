package gang.lu.riskmanagementproject.common.pdf;

/**
 * PDF文本/文件名常量
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
public interface PdfTextConstants {

    /**
     * 文件名配置
     */
    String FILENAME_PREFIX = "worker_";
    String FILENAME_MIDDLE = "_risk_report_";
    String FILENAME_SUFFIX = ".pdf";


    /**
     * PDF展示文本
     */
    String LABEL_REPORT_TITLE = "工人风险分析报告";
    String LABEL_GENERATE_TIME = "生成时间：";
    String LABEL_SECTION_INFO = "基本信息";
    String LABEL_SECTION_AI = "AI风险预测结果";
    String LABEL_SECTION_HISTORY = "历史风险记录明细";
    String LABEL_WORKER_ID = "工人ID";
    String LABEL_RECORD_COUNT = "历史记录条数";
    String LABEL_TIME_SPAN = "分析记录时间跨度";
    String LABEL_PREDICTED_LEVEL = "预测风险等级";
    String LABEL_RISK_TREND = "风险趋势";
    String LABEL_SUMMARY = "分析摘要";
    String LABEL_CONFIDENCE = "置信度说明";
    String LABEL_SUGGESTIONS = "\n改善建议：";
    String LABEL_ALERT_YES = "⚠ 是";
    String LABEL_ALERT_NO = "否";
    String LABEL_NO_DATA = "无数据";
    String LABEL_UNKNOWN = "-";
    String LABEL_FOOTER = "本报告由系统自动生成，AI预测结果仅供参考，不作为医疗诊断依据。";
    String LABEL_TIME_SPAN_CONNECTOR = " ~ ";
}