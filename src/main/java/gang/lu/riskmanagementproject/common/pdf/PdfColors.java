package gang.lu.riskmanagementproject.common.pdf;

import com.itextpdf.kernel.colors.DeviceRgb;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 16:12
 */
public class PdfColors {

    private PdfColors() {
    }

    public static final DeviceRgb THEME_BLUE  = new DeviceRgb(41,  128, 185);
    public static final DeviceRgb ROW_ALT     = new DeviceRgb(235, 245, 251);
    public static final DeviceRgb RISK_RED    = new DeviceRgb(231, 76,  60);
    public static final DeviceRgb RISK_ORANGE = new DeviceRgb(230, 126, 34);
    public static final DeviceRgb RISK_GREEN  = new DeviceRgb(39,  174, 96);
    public static final DeviceRgb RISK_BLACK  = new DeviceRgb(0,   0,   0);
}
