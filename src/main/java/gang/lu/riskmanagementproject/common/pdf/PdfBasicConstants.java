package gang.lu.riskmanagementproject.common.pdf;

/**
 * PDF 基础配置常量
 * <p>
 * 统一管理 PDF 生成所需的字体、页边距、字号、间距及表格配置。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>字体配置</li>
 *   <li>页边距</li>
 *   <li>字体大小</li>
 *   <li>间距</li>
 *   <li>表格配置</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
public interface PdfBasicConstants {

    // ==============================1. 字体配置================================

    String FONT_PATH      = "/fonts/simsun.ttc";
    int    FONT_TTC_INDEX = 0;

    // ==============================2. 页边距================================

    float MARGIN = 36f;

    // ==============================3. 字体大小================================

    float FONT_SIZE_TITLE      = 22f;
    float FONT_SIZE_SUBTITLE   = 9f;
    float FONT_SIZE_SECTION    = 13f;
    float FONT_SIZE_BODY       = 10f;
    float FONT_SIZE_TABLE      = 9f;
    float FONT_SIZE_FOOTER     = 8f;
    float FONT_SIZE_SUGGESTION = 10f;

    // ==============================4. 间距================================

    float MARGIN_BOTTOM_TITLE   = 4f;
    float MARGIN_BOTTOM_SECTION = 6f;
    float MARGIN_BOTTOM_BLOCK   = 16f;
    float SUGGESTION_LEFT_INDENT = 12f;
    float BORDER_WIDTH_1        = 1f;

    // ==============================5. 表格配置================================

    float[]  HISTORY_COL_WIDTHS = {1.2f, 2.8f, 1.5f, 1.8f, 1.8f, 1.8f, 1.2f};
    String[] HISTORY_HEADERS    = {
            "序号", "记录时间", "心率(bpm)", "呼吸率(/min)", "疲劳度(%)", "风险等级", "报警"
    };
    int TWO_COL_TABLE_LABEL_WIDTH = 3;
    int TWO_COL_TABLE_VALUE_WIDTH = 7;
    int EMPTY_CELL_COLSPAN        = HISTORY_COL_WIDTHS.length;
}