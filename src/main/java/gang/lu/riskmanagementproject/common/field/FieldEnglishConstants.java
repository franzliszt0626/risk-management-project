package gang.lu.riskmanagementproject.common.field;

/**
 * 英文字段名常量
 * <p>
 * 统一管理数据库字段名、HTTP 请求 / 响应字段名及 AI 接口字段名。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>通用字段</li>
 *   <li>工人字段</li>
 *   <li>生理指标字段</li>
 *   <li>工作区域字段</li>
 *   <li>AI 请求字段</li>
 *   <li>AI 响应字段</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface FieldEnglishConstants {

    // ==============================1. 通用字段================================

    String ID          = "id";
    String UPCAST_ID   = "ID";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";
    String COUNT       = "count";
    String PERIOD      = "period";

    // ==============================2. 工人字段================================

    String WORKER_STATUS = "status";
    String WORKER_TYPE   = "work_type";

    // ==============================3. 生理指标字段================================

    String HEART_RATE        = "heart_rate";
    String RESPIRATORY_RATE  = "respiratory_rate";
    String FATIGUE_PERCENT   = "fatigue_percent";
    String RISK_LEVEL        = "risk_level";
    String ALERT_FLAG        = "alert_flag";

    // ==============================4. 工作区域字段================================

    String AREA_RISK_LEVEL = "area_risk_level";

    // ==============================5. AI 请求字段================================

    String MODEL      = "model";
    String MAX_TOKENS = "max_tokens";
    String MESSAGES   = "messages";
    String ROLE       = "role";
    String CONTENT    = "content";
    String USER       = "user";

    // ==============================6. AI 响应字段================================

    String CHOICES                  = "choices";
    String MESSAGE                  = "message";
    String AI_FIELD_PREDICTED_RISK  = "predicted_risk_level";
    String AI_FIELD_RISK_TREND      = "risk_trend";
    String AI_FIELD_ANALYSIS        = "analysis_summary";
    String AI_FIELD_SUGGESTIONS     = "suggestions";
    String AI_FIELD_CONFIDENCE      = "confidence_note";
}