package gang.lu.riskmanagementproject.common.field;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 14:22
 * @description 英文的字段名
 */
public interface FieldEnglishConstants {
    /**
     * 通用字段
     */
    String ID = "id";
    String UPCAST_ID = "ID";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";

    /**
     * 工人
     */
    String WORKER_STATUS = "status";
    String WORKER_TYPE = "work_type";

    /**
     * 生理指标
     */
    String HEART_RATE = "heart_rate";
    String RESPIRATORY_RATE = "respiratory_rate";
    String FATIGUE_PERCENT = "fatigue_percent";
    String RISK_LEVEL = "risk_level";
    String ALERT_FLAG = "alert_flag";
}
