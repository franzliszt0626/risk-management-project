package gang.lu.riskmanagementproject.common.field;

/**
 * 中文字段名常量
 * <p>
 * 统一管理业务层及异常提示中使用的中文字段显示名称。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>通用字段</li>
 *   <li>工人字段</li>
 *   <li>风险指标字段</li>
 *   <li>工作区域字段</li>
 *   <li>预警记录字段</li>
 *   <li>AI 相关</li>
 *   <li>其他</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface FieldChineseConstants {

    // ==============================1. 通用字段================================

    String ID_LIST = "ID列表";

    // ==============================2. 工人字段================================

    String WORKER_ID      = "工人ID";
    String WORKER_ID_LIST = "工人ID列表";
    String WORKER_CODE    = "工号";
    String NAME           = "姓名";
    String WORK_TYPE      = "工种";
    String WORK_YEAR      = "工龄";
    String STATUS         = "工人状态";

    // ==============================3. 风险指标字段================================

    String RISK_INDICATOR_ID      = "风险指标ID";
    String RISK_INDICATOR_ID_LIST = "风险指标ID列表";
    String RISK_LEVEL             = "风险等级";
    String HEART_RATE             = "心率";
    String RESPIRATORY_RATE       = "呼吸率";
    String FATIGUE_PERCENT        = "疲劳百分比";
    String RISK_VERY_HIGH         = "严重风险";
    String RISK_HIGH              = "高风险";
    String RISK_MEDIUM            = "中风险";
    String UNKNOWN_PERIOD         = "未知时间段";

    // ==============================4. 工作区域字段================================

    String WORK_AREA_ID      = "工作区域ID";
    String WORK_AREA_ID_LIST = "工作区域ID列表";
    String AREA_RISK_LEVEL   = "区域风险等级";
    String WORK_AREA_CODE    = "区域编码";
    String WORK_AREA_NAME    = "区域名称";

    // ==============================5. 预警记录字段================================

    String ALERT_RECORD_ID      = "预警记录ID";
    String ALERT_RECORD_ID_LIST = "预警记录ID列表";
    String ALERT_LEVEL          = "预警等级";
    String HANDLED_BY           = "处理人";

    // ==============================6. AI 相关================================

    String AI_DEFAULT_RISK  = "未知";
    String AI_DEFAULT_TREND = "平稳";

    // ==============================7. 其他================================

    String ENUM    = "枚举值";
    String INVALID = "不合法";
    String ALLOW   = "允许值";
    String EMPTY   = "空值";
    String NUMBER  = "数字";
}