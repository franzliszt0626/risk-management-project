package gang.lu.riskmanagementproject.helper;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import gang.lu.riskmanagementproject.common.pdf.PdfColors;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static gang.lu.riskmanagementproject.common.global.GlobalFormatConstants.DEFAULT_DATE_TIME_FORMAT;
import static gang.lu.riskmanagementproject.common.global.GlobalFormatConstants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_COLON;
import static gang.lu.riskmanagementproject.common.http.HttpConstants.*;
import static gang.lu.riskmanagementproject.common.pdf.PdfBasicConstants.*;
import static gang.lu.riskmanagementproject.common.pdf.PdfColors.*;
import static gang.lu.riskmanagementproject.common.pdf.PdfLogConstants.*;
import static gang.lu.riskmanagementproject.common.pdf.PdfTextConstants.*;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.PDF_FONT_NOT_FOUND;
import static gang.lu.riskmanagementproject.message.FailedMessages.PDF_GENERATE_FAILED;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 16:16
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PdfHelper {

    // ======================== PDF生成入口 ========================

    public void writePdfToResponse(Long workerId, List<RiskIndicatorVO> history,
                                   RiskPredictionVO prediction, HttpServletResponse response) {
        log.info(LOG_RESPONSE_COMMITTED, response.isCommitted());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            // 第一步：加载字体
            PdfFont font = loadChineseFont();

            // 手动创建PDF相关对象，避免try-with-resources提前关闭
            PdfWriter writer = new PdfWriter(buffer);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf, PageSize.A4);

            // 设置页边距
            doc.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);

            // 渲染所有区块（增加空数据兜底）
            renderTitle(doc, font);
            renderWorkerInfo(doc, font, workerId, history);
            renderAiPrediction(doc, font, prediction);
            renderHistoryTable(doc, font, history);
            renderFooter(doc, font);

            // 手动关闭文档，确保内容刷入缓冲区
            doc.close();
            pdf.close();
            writer.close();

            // 第二步：设置响应头并写出
            setResponseHeaders(workerId, response);
            response.setContentLength(buffer.size());

            // 安全写出流
            buffer.writeTo(response.getOutputStream());
            response.getOutputStream().flush();

            log.info(LOG_PDF_EXPORT_SUCCESS, workerId, buffer.size() / 1024);

        } catch (IOException e) {
            log.error(LOG_PDF_GENERATE_FAILED, workerId, e);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, PDF_GENERATE_FAILED);
        } finally {
            // 关闭缓冲区，释放资源
            IoUtil.close(buffer);
        }
    }

    // ======================== PDF各区块渲染========================

    /**
     * 渲染标题区
     */
    private void renderTitle(Document doc, PdfFont font) {
        // 标题
        Paragraph titlePara = new Paragraph(LABEL_REPORT_TITLE)
                .setFont(font).setFontSize(FONT_SIZE_TITLE).setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(THEME_BLUE)
                .setMarginBottom(MARGIN_BOTTOM_TITLE);

        // 生成时间
        Paragraph timePara = new Paragraph(LABEL_GENERATE_TIME
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .setFont(font).setFontSize(FONT_SIZE_SUBTITLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(GRAY_50)
                .setMarginBottom(MARGIN_BOTTOM_BLOCK);

        // 强制添加，确保至少有基础内容
        doc.add(titlePara);
        doc.add(timePara);
    }

    /**
     * 渲染基本信息区（增加空值判断）
     */
    public void renderWorkerInfo(Document doc, PdfFont font,
                                 Long workerId, List<RiskIndicatorVO> history) {
        doc.add(buildSectionTitle(LABEL_SECTION_INFO, font));

        Table table = buildTwoColTable();
        // 空值兜底
        String workerIdStr = workerId != null ? String.valueOf(workerId) : LABEL_UNKNOWN;
        String recordCountStr = history != null ? String.valueOf(history.size()) : "0";
        String timeSpanStr = history != null ? resolveTimeSpan(history) : LABEL_NO_DATA;

        addInfoRow(table, font, LABEL_WORKER_ID, workerIdStr);
        addInfoRow(table, font, LABEL_RECORD_COUNT, recordCountStr);
        addInfoRow(table, font, LABEL_TIME_SPAN, timeSpanStr);

        doc.add(table);
        doc.add(new Paragraph("\n"));
    }

    /**
     * 渲染AI预测区（无预测时跳过）
     */
    public void renderAiPrediction(Document doc, PdfFont font, RiskPredictionVO prediction) {
        if (prediction == null) {
            return;
        }
        doc.add(buildSectionTitle(LABEL_SECTION_AI, font));

        Table table = buildTwoColTable();
        // 空值兜底
        addInfoRow(table, font, LABEL_PREDICTED_LEVEL,
                ObjectUtil.isNotNull(prediction.getPredictedRiskLevel()) ? prediction.getPredictedRiskLevel() : LABEL_UNKNOWN);
        addInfoRow(table, font, LABEL_RISK_TREND,
                ObjectUtil.isNotNull(prediction.getRiskTrend()) ? prediction.getRiskTrend() : LABEL_UNKNOWN);
        addInfoRow(table, font, LABEL_SUMMARY,
                ObjectUtil.isNotNull(prediction.getAnalysisSummary()) ? prediction.getAnalysisSummary() : LABEL_UNKNOWN);
        addInfoRow(table, font, LABEL_CONFIDENCE,
                ObjectUtil.isNotNull(prediction.getConfidenceNote()) ? prediction.getConfidenceNote() : LABEL_UNKNOWN);

        doc.add(table);
        renderSuggestions(doc, font, prediction);
        doc.add(new Paragraph("\n"));
    }

    /**
     * 渲染改善建议列表
     */
    public void renderSuggestions(Document doc, PdfFont font, RiskPredictionVO prediction) {
        if (ObjectUtil.isNull(prediction.getSuggestions()) || prediction.getSuggestions().isEmpty()) {
            return;
        }
        doc.add(new Paragraph(LABEL_SUGGESTIONS)
                .setFont(font).setFontSize(FONT_SIZE_BODY).setBold()
                .setFontColor(RISK_BLACK));

        List<String> suggestions = prediction.getSuggestions();
        for (int i = 0; i < suggestions.size(); i++) {
            doc.add(new Paragraph((i + 1) + ". " + suggestions.get(i))
                    .setFont(font).setFontSize(FONT_SIZE_SUGGESTION)
                    .setFontColor(RISK_BLACK)
                    .setMarginLeft(SUGGESTION_LEFT_INDENT));
        }
    }

    /**
     * 渲染历史记录明细表（增加空数据兜底）
     */
    public void renderHistoryTable(Document doc, PdfFont font, List<RiskIndicatorVO> history) {
        doc.add(buildSectionTitle(LABEL_SECTION_HISTORY, font));

        Table table = new Table(UnitValue.createPercentArray(HISTORY_COL_WIDTHS))
                .setWidth(UnitValue.createPercentValue(100));

        // 添加表头
        addHistoryTableHeader(table, font);

        // 空数据处理：显示无数据行
        if (ObjectUtil.isNull(history) || history.isEmpty()) {
            Cell emptyCell = new Cell(1, EMPTY_CELL_COLSPAN)
                    .add(new Paragraph(LABEL_NO_DATA)
                            .setFont(font).setFontSize(FONT_SIZE_TABLE)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(emptyCell);
        } else {
            // 正常添加数据行
            addHistoryTableRows(table, font, history);
        }

        doc.add(table);
        doc.add(new Paragraph("\n"));
    }

    /**
     * 渲染页脚
     */
    public void renderFooter(Document doc, PdfFont font) {
        doc.add(new Paragraph(LABEL_FOOTER)
                .setFont(font).setFontSize(FONT_SIZE_FOOTER)
                .setFontColor(GRAY_50)
                .setTextAlignment(TextAlignment.CENTER));
    }

    // ======================== 历史表格构建 ========================

    private void addHistoryTableHeader(Table table, PdfFont font) {
        for (String header : HISTORY_HEADERS) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(header)
                            .setFont(font).setFontSize(FONT_SIZE_TABLE).setBold()
                            .setFontColor(PdfColors.WHITE))
                    .setBackgroundColor(THEME_BLUE)
                    .setTextAlignment(TextAlignment.CENTER));
        }
    }

    public void addHistoryTableRows(Table table, PdfFont font, List<RiskIndicatorVO> history) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        for (int i = 0; i < history.size(); i++) {
            RiskIndicatorVO r = history.get(i);
            boolean alt = (i % 2 == 1);

            addHistoryCell(table, font, String.valueOf(i + 1), alt);
            addHistoryCell(table, font,
                    ObjectUtil.isNotNull(r.getCreateTime()) ? r.getCreateTime().format(fmt) : LABEL_UNKNOWN, alt);
            addHistoryCell(table, font,
                    ObjectUtil.isNotNull(r.getHeartRate()) ? String.valueOf(r.getHeartRate()) : LABEL_UNKNOWN, alt);
            addHistoryCell(table, font,
                    ObjectUtil.isNotNull(r.getRespiratoryRate()) ? String.valueOf(r.getRespiratoryRate()) : LABEL_UNKNOWN, alt);
            addHistoryCell(table, font,
                    ObjectUtil.isNotNull(r.getFatiguePercent()) ? String.format("%.2f", r.getFatiguePercent()) : LABEL_UNKNOWN, alt);
            addRiskLevelCell(table, font, r, alt);
            addHistoryCell(table, font,
                    Boolean.TRUE.equals(r.getAlertFlag()) ? LABEL_ALERT_YES : LABEL_ALERT_NO, alt);
        }
    }

    /**
     * 风险等级单元格（带颜色）
     */
    public void addRiskLevelCell(Table table, PdfFont font, RiskIndicatorVO r, boolean alt) {
        String levelStr = ObjectUtil.isNotNull(r.getRiskLevel()) ? r.getRiskLevel().getValue() : LABEL_UNKNOWN;
        Cell cell = new Cell()
                .add(new Paragraph(levelStr)
                        .setFont(font).setFontSize(FONT_SIZE_TABLE).setBold()
                        .setFontColor(resolveRiskColor(levelStr)))
                .setTextAlignment(TextAlignment.CENTER);
        if (alt) {
            cell.setBackgroundColor(ROW_ALT);
        }
        table.addCell(cell);
    }

    // ======================== 通用构建工具 ========================

    public Paragraph buildSectionTitle(String title, PdfFont font) {
        return new Paragraph(title)
                .setFont(font).setFontSize(FONT_SIZE_SECTION).setBold()
                .setFontColor(THEME_BLUE)
                .setBorderBottom(new SolidBorder(THEME_BLUE, BORDER_WIDTH_1))
                .setMarginBottom(MARGIN_BOTTOM_SECTION);
    }

    public Table buildTwoColTable() {
        return new Table(UnitValue.createPercentArray(new float[]{TWO_COL_TABLE_LABEL_WIDTH, TWO_COL_TABLE_VALUE_WIDTH}))
                .setWidth(UnitValue.createPercentValue(100));
    }

    public void addInfoRow(Table table, PdfFont font, String label, String value) {
        table.addCell(new Cell()
                .add(new Paragraph(label)
                        .setFont(font).setFontSize(FONT_SIZE_BODY).setBold()
                        .setFontColor(RISK_BLACK))
                .setBackgroundColor(ROW_ALT));
        table.addCell(new Cell()
                .add(new Paragraph(ObjectUtil.isNotNull(value) ? value : LABEL_UNKNOWN)
                        .setFont(font).setFontSize(FONT_SIZE_BODY)
                        .setFontColor(RISK_BLACK)));
    }

    public void addHistoryCell(Table table, PdfFont font, String value, boolean alt) {
        Cell cell = new Cell()
                .add(new Paragraph(value)
                        .setFont(font).setFontSize(FONT_SIZE_TABLE)
                        .setFontColor(RISK_BLACK))
                .setTextAlignment(TextAlignment.CENTER);
        if (alt) {
            cell.setBackgroundColor(ROW_ALT);
        }
        table.addCell(cell);
    }

    // ======================== 小工具方法 ========================

    private PdfFont loadChineseFont() throws IOException {
        try (InputStream is = getClass().getResourceAsStream(FONT_PATH)) {
            if (ObjectUtil.isNull(is)) {
                throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format(PDF_FONT_NOT_FOUND, FONT_PATH));
            }
            byte[] fontBytes = IoUtil.readBytes(is);

            // 从内存字节加载TTC字体（指定index=0），避免临时文件问题
            FontProgram fontProgram = FontProgramFactory.createFont(fontBytes, FONT_TTC_INDEX, false);

            PdfFont font = PdfFontFactory.createFont(
                    fontProgram,
                    PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
            );
            log.info(LOG_FONT_LOAD_SUCCESS, font.getFontProgram().getFontNames().getFontName());
            return font;
        }
    }

    private void setResponseHeaders(Long workerId, HttpServletResponse response) throws IOException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_WITHOUT_COLON));
        String filename = FILENAME_PREFIX + workerId + FILENAME_MIDDLE + timestamp + FILENAME_SUFFIX;

        // 完善响应头，兼容各浏览器
        response.reset();
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setHeader(CACHE_CONTROL, CACHE_CONTROL_HEADER);
        response.setHeader(PRAGMA, PRAGMA_HEADER);
        response.setHeader(EXPIRES, EXPIRES_HEADER);
        response.setHeader(CONTENT_DISPOSITION_KEY,
                ATTACHMENT_FILENAME + filename + "\"");
    }

    public String resolveTimeSpan(List<RiskIndicatorVO> history) {
        if (ObjectUtil.isNull(history) || history.isEmpty()) {
            return LABEL_NO_DATA;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        return history.get(0).getCreateTime().format(fmt)
                + LABEL_TIME_SPAN_CONNECTOR
                + history.get(history.size() - 1).getCreateTime().format(fmt);
    }

    private DeviceRgb resolveRiskColor(String riskLevel) {
        if (ObjectUtil.isNull(riskLevel)) {
            return RISK_BLACK;
        }
        switch (riskLevel) {
            case RISK_VERY_HIGH:
            case RISK_HIGH:
                return RISK_RED;
            case RISK_MEDIUM:
                return RISK_ORANGE;
            default:
                return RISK_GREEN;
        }
    }
}
