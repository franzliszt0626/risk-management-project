package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 17:00
 * @description 失败信息集合
 */
public class FailureMessages {
    /**
     * 通用异常信息
     */
    public static final String DATABASE_ERROR_MESSAGE = "数据库操作失败，请稍后重试！";
    public static final String PARAMETER_VERIFY_ERROR_MESSAGE = "参数校验失败，请重试！";
    public static final String REQUEST_PARAMETER_ERROR_MESSAGE = "请求参数错误，请重试！";
    public static final String UNIVERSAL_ERROR_MESSAGE = "系统内部错误，请联系管理员！";
    public static final String INVALID_ID_ERROR_MESSAGE = "无效的id！";

    /**
     * 工人相关异常信息
     */
    public static final String CREATE_WORKER_ERROR_MESSAGE = "创建工人失败！";
    public static final String WORKER_NOT_EXISTING_ERROR_MESSAGE = "工人不存在！";
    public static final String DUPLICATE_WORKER_ID_ERROR_MESSAGE = "工号不得重复！";
    public static final String WORKER_ID_NOT_EXISTING_ERROR_MESSAGE = "工号不能为空！";


    /**
     * 风险管理相关异常信息
     */
    public static final String INVALID_HEARTRATE_ERROR_MESSAGE = "无效的心率！心率不能为负数或者为空！";
    public static final String INVALID_RESPIRATORY_RATE_ERROR_MESSAGE = "无效的呼吸率！呼吸率不能为负数或者为空！";
    public static final String INVALID_FATIGUE_PERCENT_ERROR_MESSAGE = "无效的疲劳率！疲劳百分比应在 0～100% 之间！";
    public static final String CREATE_RISK_INDICATOR_ERROR_MESSAGE = "创建风险信息失败！";
    public static final String NON_RISK_LEVEL_ERROR_MESSAGE = "风险等级必须不能为空！";
    public static final String LATEST_RISK_INDICATOR_NOT_EXISTING_ERROR_MESSAGE = "记录不存在！";

    /**
     * 工作区域相关方法
     */
    public static final String EMPTY_WORK_AREA_CODE_ERROR_MESSAGE = "工作区域的编号不能为空！";
    public static final String DUPLICATE_WORK_AREA_CODE_ERROR_MESSAGE = "重复的工作区域编号！";
    public static final String EMPTY_WORK_AREA_ID_ERROR_MESSAGE = "区域ID不能为空！";
    public static final String MISSING_WORK_AREA_ERROR_MESSAGE = "工作区域不存在！";
    public static final String MISSING_KEY_WORK_AREA_PARAMETER_ERROR_MESSAGE = "ID或修改参数不能为空！";

    public static final String ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE = "预警记录不存在！";
    public static final String WORKER_ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE = "该工人暂无预警记录！";
    public static final String FAILED_HANDLED_ERROR_MESSAGE = "标记已处理失败！";
}
