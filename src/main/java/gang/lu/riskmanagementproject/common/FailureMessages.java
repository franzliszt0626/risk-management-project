package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 17:00
 * @description 失败信息集合
 */
public class FailureMessages {

    private FailureMessages() {
    }

    /**
     * 通用异常
     */
    public static final String COMMON_DATABASE_ERROR = "数据库操作失败，请稍后重试！";
    public static final String COMMON_PARAM_VERIFY_ERROR = "参数校验失败，请检查参数后重试！";
    public static final String COMMON_REQUEST_PARAM_ERROR = "请求参数错误，请检查参数格式！";
    public static final String COMMON_SYSTEM_ERROR = "系统内部错误，请联系管理员！";
    public static final String COMMON_INVALID_ID_ERROR = "无效的ID，请确认ID是否正确！";

    /**
     * JSON解析相关
     */
    public static final String COMMON_JSON_PARSE_ERROR = "JSON解析失败，请检查请求参数格式！";
    public static final String COMMON_JSON_ILLEGAL_ARG_ERROR = "JSON参数不合法：%s";

    /**
     * 运行时异常
     */
    public static final String COMMON_NULL_POINTER_ERROR = "空指针异常，请联系管理员排查！";
    public static final String COMMON_RUNTIME_ERROR = "运行时异常：%s";

    /**
     * 绑定异常
     */
    public static final String COMMON_BIND_PARAM_ERROR = "请求参数绑定失败，请检查参数格式！";

    /**
     * 工人相关
     */
    public static final String WORKER_OPERATE_CREATE_ERROR = "创建工人信息失败！";
    public static final String WORKER_OPERATE_DELETE_ERROR = "删除工人信息失败！";
    public static final String WORKER_OPERATE_UPDATE_ERROR = "更新工人信息失败！";
    public static final String WORKER_DATA_NOT_EXIST = "工人不存在，请确认工人ID是否正确！";
    public static final String WORKER_PARAM_DUPLICATE_CODE = "工号重复，请更换工号后重试！";
    public static final String WORKER_PARAM_EMPTY_CODE = "工号不能为空！";
    public static final String WORKER_PARAM_EMPTY_NAME = "姓名不能为空！";
    public static final String RISK_PARAM_EMPTY_WORKER_ID = "工人ID不能为空！";
    public static final String RISK_DATA_WORKER_NOT_EXIST = "工人ID不存在，当前值：%s！";
    public static final String WORKER_NOT_EXIST = "工人ID不存在：%s";

    /**
     * 风险指标相关
     */
    public static final String RISK_PARAM_INVALID_HEART_RATE = "无效的心率！心率不能为负数或空值！";
    public static final String RISK_PARAM_INVALID_RESPIRATORY_RATE = "无效的呼吸率！呼吸率不能为负数或空值！";
    public static final String RISK_PARAM_INVALID_FATIGUE_PERCENT = "无效的疲劳率！疲劳百分比应在 0～100% 之间！";
    public static final String RISK_OPERATE_CREATE_ERROR = "创建风险指标记录失败！";
    public static final String RISK_PARAM_EMPTY_RISK_LEVEL = "风险等级不能为空！";
    public static final String RISK_PARAM_INVALID_RISK_LEVEL = "无效的风险等级：%s！允许值为：低风险、中风险、高风险、严重风险";
    public static final String RISK_DATA_NOT_EXIST = "风险指标记录不存在！";
    public static final String RISK_INDICATOR_NO_LATEST = "工人ID：%s 暂无最新风险指标记录！";


    /**
     * 工作区域相关
     */
    public static final String WORK_AREA_PARAM_EMPTY_CODE = "工作区域编号不能为空！";
    public static final String WORK_AREA_PARAM_EMPTY_NAME = "工作区域名称不能为空！";
    public static final String WORK_AREA_PARAM_DUPLICATE_CODE = "工作区域编号重复，请更换编号！";
    public static final String WORK_AREA_PARAM_EMPTY_ID = "工作区域ID不能为空！";
    public static final String WORK_AREA_DATA_NOT_EXIST = "工作区域不存在，请确认区域ID是否正确！";


    /**
     * 预警记录相关
     */
    public static final String ALERT_DATA_NOT_FOUND = "预警记录不存在！";
    public static final String ALERT_DATA_WORKER_NOT_FOUND = "该工人暂无预警记录！";
    public static final String ALERT_OPERATE_HANDLE_ERROR = "标记预警记录已处理失败！";
    public static final String ALERT_PARAM_EMPTY_LEVEL = "预警等级不能为空！";
    public static final String ALERT_PARAM_INVALID_LEVEL = "无效的预警等级：%s！允许值为：警告、严重";


}
