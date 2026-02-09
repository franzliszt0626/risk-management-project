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

    // ======================== 通用基础异常 ========================

    /**
     * 数据库操作失败
     */
    public static final String COMMON_DATABASE_ERROR = "【数据库操作失败】数据库操作失败，请稍后重试！";
    /**
     * 参数校验失败
     */
    public static final String COMMON_PARAM_VERIFY_ERROR = "【参数校验失败】参数校验失败，请检查参数后重试！";
    /**
     * 系统内部错误
     */
    public static final String COMMON_SYSTEM_ERROR = "【系统内部错误】系统内部错误，请联系管理员！";
    /**
     * 无效ID（通用）
     */
    public static final String COMMON_INVALID_ID_ERROR = "【参数校验失败】无效的%s，请确认ID是否正确！";
    /**
     * 参数为空（通用）
     */
    public static final String COMMON_PARAM_EMPTY_ERROR = "【%s失败】%s不能为空！";


    // ======================== JSON解析异常 ========================
    /**
     * JSON解析失败
     */
    public static final String COMMON_JSON_PARSE_ERROR = "【参数解析失败】JSON解析失败，请检查请求参数格式！";

    // ======================== 运行时异常 ========================
    /**
     * 空指针异常
     */
    public static final String COMMON_NULL_POINTER_ERROR = "【运行时异常】空指针异常，请联系管理员排查！";
    /**
     * 通用运行时异常
     */
    public static final String COMMON_RUNTIME_ERROR = "【运行时异常】运行时异常：%s！";

    // ======================== 参数绑定异常 ========================
    /**
     * 请求参数绑定失败
     */
    public static final String COMMON_BIND_PARAM_ERROR = "【参数绑定失败】请求参数绑定失败，请检查参数格式！";

    // ======================== 工人业务异常 ========================
    /**
     * 创建工人失败
     */
    public static final String WORKER_CREATE_ERROR = "【创建工人失败】创建工人信息失败！";
    /**
     * 删除工人失败
     */
    public static final String WORKER_DELETE_ERROR = "【删除工人失败】删除工人信息失败！";
    /**
     * 批量删除工人失败
     */
    public static final String WORKER_DELETE_BATCH_ERROR = "【删除工人失败】批量删除工人失败！";
    /**
     * 批量删除的工人不存在
     */
    public static final String WORKER_NOT_EXIST_BATCH = "【删除工人失败】批量删除工人失败！不存在的id：%s";
    /**
     * 更新工人失败
     */
    public static final String WORKER_UPDATE_ERROR = "【更新工人失败】更新工人信息失败！";
    /**
     * 工人不存在（通用）
     */
    public static final String WORKER_NOT_EXIST = "【参数校验失败】工人不存在，请确认工人ID是否正确！";
    /**
     * 工人不存在（带操作和ID）
     */
    public static final String WORKER_NOT_EXIST_WITH_PARAM = "【%s失败】工人不存在，ID：%s！";
    /**
     * 工号重复
     */
    public static final String WORKER_CODE_DUPLICATE = "【%s失败】工号重复：%s！";
    /**
     * 工种不能为空
     */
    public static final String WORKER_TYPE_EMPTY = "【%s失败】工种不能为空！";
    /**
     * 批量删除的id集合为空
     */
    public static final String WORKER_DELETE_BATCH_ID_EMPTY = "【删除工人失败】批量删除的工人ID列表不能为空！";
    /**
     * 工人状态不能为空
     */
    public static final String WORKER_STATUS_EMPTY = "【%s失败】状态不能为空！";
    /**
     * 无效的工种
     */
    public static final String WORKER_TYPE_INVALID = "【参数校验失败】无效的工种：%s！允许值为：高空作业, 受限空间, 设备操作, 正常作业！";
    /**
     * 无效的工人状态
     */
    public static final String WORKER_STATUS_INVALID = "【参数校验失败】无效的状态：%s！允许值为：正常, 异常, 离线！";

    // ======================== 风险指标业务异常 ========================
    /**
     * 心率参数无效
     */
    public static final String RISK_HEART_RATE_INVALID = "【参数校验失败】无效的心率！心率不能为负数或空值！";
    /**
     * 呼吸率参数无效
     */
    public static final String RISK_RESPIRATORY_RATE_INVALID = "【参数校验失败】无效的呼吸率！呼吸率不能为负数或空值！";
    /**
     * 疲劳百分比参数无效
     */
    public static final String RISK_FATIGUE_PERCENT_INVALID = "【参数校验失败】无效的疲劳率！疲劳百分比应在 0～100% 之间！";
    /**
     * 创建风险指标失败
     */
    public static final String RISK_CREATE_ERROR = "【创建风险指标失败】创建风险指标记录失败！";
    /**
     * 风险等级不能为空
     */
    public static final String RISK_LEVEL_EMPTY = "【%s失败】风险等级不能为空！";


    /**
     * 无效的风险等级
     */
    public static final String RISK_LEVEL_INVALID = "【参数校验失败】无效的风险等级：%s！允许值为：低风险、中风险、高风险、严重风险！";


    // ======================== 工作区域业务异常 ========================
    /**
     * 区域风险等级为空
     */
    public static final String WORK_AREA_RISK_LEVEL_EMPTY = "【%s失败】区域风险等级不能为空！";
    /**
     * 区域编号重复
     */
    public static final String WORK_AREA_CODE_DUPLICATE = "【%s失败】工作区域编号重复，请更换编号！";
    /**
     * 工作区域不存在
     */
    public static final String WORK_AREA_NOT_EXIST = "【参数校验失败】工作区域不存在，请确认区域ID是否正确！";
    /**
     * 无效的区域风险等级
     */
    public static final String WORK_AREA_RISK_LEVEL_INVALID = "【参数校验失败】无效的工作区域风险等级：%s！允许值为：低风险、中风险、高风险！";


    // ======================== 预警记录业务异常 ========================
    /**
     * 预警记录不存在
     */
    public static final String ALERT_RECORD_NOT_EXIST = "【参数校验失败】预警记录不存在！";
    /**
     * 标记预警已处理失败
     */
    public static final String ALERT_RECORD_HANDLE_ERROR = "【处理预警记录失败】标记预警记录已处理失败！";
    /**
     * 预警等级不能为空
     */
    public static final String ALERT_LEVEL_EMPTY = "【%s失败】预警等级不能为空！";
    /**
     * 无效的预警等级
     */
    public static final String ALERT_LEVEL_INVALID = "【参数校验失败】无效的预警等级：%s！允许值为：警告、严重！";


}
