package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/7 14:46
 * @description 时间常量
 */
public class TimeConstants {
    private TimeConstants() {
    }

    /**
     * 定义6个4小时时间段（0-24点）
     */
    public static final String[] PERIOD_DESC = {
            "00:00-04:00",
            "04:00-08:00",
            "08:00-12:00",
            "12:00-16:00",
            "16:00-20:00",
            "20:00-24:00"
    };

    public static final int TIME_PERIOD_DESC_LENGTH = PERIOD_DESC.length;


}
