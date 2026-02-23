package gang.lu.riskmanagementproject.common.pdf;

/**
 * PDF日志专用常量
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
public interface PdfLogConstants {

    /**
     * PDF日志模板
     */
    String LOG_RESPONSE_COMMITTED = "响应是否已提交: {}";
    String LOG_PDF_EXPORT_SUCCESS = "PDF导出成功 workerId={}, size={}KB";
    String LOG_PDF_GENERATE_FAILED = "PDF生成失败 workerId={}";
    String LOG_FONT_LOAD_SUCCESS = "字体加载成功，字体名: {}";
}