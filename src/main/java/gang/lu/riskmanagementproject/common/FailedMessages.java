package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 17:00
 * @description 失败信息集合
 */
public interface FailedMessages {

    // ======================== 通用失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String COMMON_DATABASE_ERROR = "【数据库操作失败】数据库操作失败，请稍后重试！";
    String COMMON_PARAM_VERIFY_ERROR = "【参数校验失败】参数校验失败，请检查参数后重试！";
    String COMMON_SYSTEM_ERROR = "【系统内部错误】系统内部错误，请联系管理员！";
    String COMMON_INVALID_ID_ERROR = "【参数校验失败】无效的%s，请确认ID是否正确！";
    String COMMON_INVALID_ID_ERROR_WITHOUT_PREFIX = "无效的%s，请确认ID是否正确！";
    String COMMON_PARAM_EMPTY_ERROR = "【%s失败】%s不能为空！";
    String COMMON_PARAM_EMPTY_ERROR_WITHOUT_PREFIX = "%s不能为空！";
    String COMMON_JSON_PARSE_ERROR = "【参数解析失败】JSON解析失败，请检查请求参数格式！";
    String COMMON_NULL_POINTER_ERROR = "【运行时异常】空指针异常，请联系管理员排查！";
    String COMMON_RUNTIME_ERROR = "【运行时异常】运行时异常：%s！";
    String COMMON_BIND_PARAM_ERROR = "【参数绑定失败】请求参数绑定失败，请检查参数格式！";
    String COMMON_ENUM_CONVERT_ERROR = "【参数转换失败】枚举转换异常！";
    String COMMON_TIME_INVALID_ERROR = "【参数校验失败】记录开始时间不能晚于结束时间！";

    // ======================== 工人业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORKER_CREATE_ERROR = "【创建工人失败】创建工人信息失败！";
    String WORKER_DELETE_ERROR = "【删除工人失败】删除工人信息失败！";
    String WORKER_DELETE_BATCH_ERROR = "【删除工人失败】批量删除工人失败！";
    String WORKER_UPDATE_ERROR = "【更新工人失败】更新工人信息失败！";
    String WORKER_NOT_EXIST = "【参数校验失败】工人不存在，请确认工人ID是否正确！";
    String WORKER_NOT_EXIST_WITH_PARAM = "【%s失败】工人不存在，ID：%s！";
    String WORKER_NOT_EXIST_BATCH = "【删除工人失败】批量删除工人失败！不存在的id：%s";
    String WORKER_CODE_DUPLICATE = "【%s失败】工号重复：%s！";
    String WORKER_TYPE_EMPTY = "【参数校验失败】工种不能为空！";
    String WORKER_DELETE_BATCH_ID_EMPTY = "【删除工人失败】批量删除的工人ID列表不能为空！";
    String WORKER_STATUS_EMPTY = "【参数校验失败】状态不能为空！";
    String WORKER_TYPE_INVALID = "【参数校验失败】无效的工种！允许值为：高空作业, 受限空间, 设备操作, 正常作业！";
    String WORKER_STATUS_INVALID = "【参数校验失败】无效的状态！允许值为：正常, 异常, 离线！";

    // ======================== 风险指标业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String RISK_HEART_RATE_EMPTY = "【参数校验失败】无效的心率！心率不能为空值！";
    String RISK_HEART_RATE_INVALID = "【参数校验失败】无效的心率！心率需在1-300bpm范围内！";
    String RISK_RESPIRATORY_RATE_EMPTY = "【参数校验失败】无效的呼吸率！呼吸率不能为空值！";
    String RISK_RESPIRATORY_RATE_INVALID = "【参数校验失败】无效的呼吸率！呼吸率需在1-60次/min范围内！";
    String RISK_FATIGUE_PERCENT_INVALID = "【参数校验失败】无效的疲劳率！疲劳百分比应在 0～100% 之间！";
    String RISK_FATIGUE_PERCENT_EMPTY = "【参数校验失败】疲劳百分比不能为空！";
    String RISK_CREATE_ERROR = "【创建风险指标失败】创建风险指标记录失败！";
    String RISK_LEVEL_EMPTY = "【参数校验失败】风险等级不能为空！";
    String RISK_LEVEL_INVALID = "【参数校验失败】无效的风险等级！允许值为：低风险、中风险、高风险、严重风险！";

    // ======================== 工作区域业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORK_AREA_RISK_LEVEL_EMPTY = "【参数校验失败】区域风险等级不能为空！";
    String WORK_AREA_CODE_DUPLICATE = "【%s失败】工作区域编号重复，请更换编号！";
    String WORK_AREA_NOT_EXIST = "【参数校验失败】工作区域不存在，请确认区域ID是否正确！";
    String WORK_AREA_RISK_LEVEL_INVALID = "【参数校验失败】无效的工作区域风险等级！允许值为：低风险、中风险、高风险！";

    // ======================== 预警记录业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ALERT_RECORD_NOT_EXIST = "【参数校验失败】预警记录不存在！";
    String ALERT_RECORD_HANDLE_ERROR = "【处理预警记录失败】标记预警记录已处理失败！";
    String ALERT_LEVEL_EMPTY = "【参数校验失败】预警等级不能为空！";
    String ALERT_LEVEL_INVALID = "【参数校验失败】无效的预警等级！允许值为：警告、严重！";
}