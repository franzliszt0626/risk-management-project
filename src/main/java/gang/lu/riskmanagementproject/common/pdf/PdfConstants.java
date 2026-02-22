package gang.lu.riskmanagementproject.common.pdf;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 16:09
 * @description pdf常量
 */
public interface PdfConstants {

    //  字体
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String FONT_PATH = "/fonts/simsun.ttc";

    // 文件名
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String FILENAME_PREFIX = "worker_";
    String FILENAME_MIDDLE = "_risk_report_";
    String FILENAME_SUFFIX = ".pdf";
    String FILENAME_DATE_FMT = "yyyyMMddHHmmss";
    String DISPLAY_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    // HTTP响应头
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String CONTENT_TYPE = "application/pdf";
    String CONTENT_DISPOSITION_KEY = "Content-Disposition";

    // 页边距
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    float MARGIN = 36f;

    // 字体大小
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    float FONT_SIZE_TITLE = 22f;
    float FONT_SIZE_SUBTITLE = 9f;
    float FONT_SIZE_SECTION = 13f;
    float FONT_SIZE_BODY = 10f;
    float FONT_SIZE_TABLE = 9f;
    float FONT_SIZE_FOOTER = 8f;
    float FONT_SIZE_SUGGESTION = 10f;

    //  间距
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    float MARGIN_BOTTOM_TITLE = 4f;
    float MARGIN_BOTTOM_SECTION = 6f;
    float MARGIN_BOTTOM_BLOCK = 16f;
    float SUGGESTION_LEFT_INDENT = 12f;

    // 表格列宽（历史记录表，共7列）
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    float[] HISTORY_COL_WIDTHS = {1.2f, 2.8f, 1.5f, 1.8f, 1.8f, 1.8f, 1.2f};

    // 历史记录表表头
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String[] HISTORY_HEADERS = {
            "序号", "记录时间", "心率(bpm)", "呼吸率(/min)", "疲劳度(%)", "风险等级", "报警"
    };

    // 文字常量
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
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

    //  风险等级文字（对应枚举value，用于switch）
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String RISK_VERY_HIGH = "严重风险";
    String RISK_HIGH = "高风险";
    String RISK_MEDIUM = "中风险";
}
