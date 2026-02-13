package gang.lu.riskmanagementproject.common;

/**
 * <p>
 * 失败信息集合
 * </p>
 * 所有业务/系统失败提示的标准化文案，格式统一为：【错误类型】错误描述
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface FailedMessages {

    // ======================== 通用失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String COMMON_DATABASE_ERROR = "【数据库操作失败】数据库操作异常，请稍后重试！";
    String COMMON_PARAM_VERIFY_ERROR = "【参数校验失败】请求参数校验不通过，请检查参数后重试！";
    String COMMON_SYSTEM_ERROR = "【系统内部错误】系统服务异常，请联系管理员处理！";
    String COMMON_INVALID_ID_ERROR = "【参数校验失败】无效的%sID，请确认ID是否正确！";
    String COMMON_INVALID_ID_ERROR_WITHOUT_PREFIX = "无效的%sID，请确认ID是否正确！";
    String COMMON_PARAM_EMPTY_ERROR = "【参数校验失败】%s不能为空！";
    String COMMON_PARAM_EMPTY_ERROR_WITHOUT_PREFIX = "%s不能为空！";
    String COMMON_JSON_PARSE_ERROR = "【参数解析失败】JSON格式解析异常，请检查请求参数格式！";
    String COMMON_NULL_POINTER_ERROR = "【运行时异常】系统空指针异常，请联系管理员排查！";
    String COMMON_RUNTIME_ERROR = "【运行时异常】系统运行异常：%s！";
    String COMMON_BIND_PARAM_ERROR = "【参数绑定失败】请求参数绑定异常，请检查参数格式！";
    String COMMON_ENUM_CONVERT_ERROR = "【参数转换失败】枚举类型转换异常，请检查参数值！";
    String COMMON_TIME_INVALID_ERROR = "【参数校验失败】开始时间不能晚于结束时间！";
    String COMMON_MIN_MAX_INVALID_ERROR = "【参数校验失败】%s最小值不能大于最大值！";

    // ======================== 工人业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORKER_CREATE_ERROR = "【工人操作失败】创建工人信息失败，请稍后重试！";
    String WORKER_DELETE_ERROR = "【工人操作失败】删除工人信息失败，请稍后重试！";
    String WORKER_DELETE_BATCH_ERROR = "【工人操作失败】批量删除工人信息失败，请稍后重试！";
    String WORKER_UPDATE_ERROR = "【工人操作失败】更新工人信息失败，请稍后重试！";
    String WORKER_NOT_EXIST = "【参数校验失败】工人信息不存在，请确认工人ID是否正确！";
    String WORKER_NOT_EXIST_BY_CODE = "【参数校验失败】工人信息不存在，请确认工人工号是否正确！";
    String WORKER_NOT_EXIST_WITH_PARAM = "【参数校验失败】工人信息不存在，ID：%s！";
    String WORKER_NOT_EXIST_BATCH = "【工人操作失败】批量删除工人失败，请校验ID合法性！";
    String WORKER_CODE_DUPLICATE = "【参数校验失败】工号重复：%s，请更换工号！";
    String WORKER_CODE_EMPTY = "【参数校验失败】工号不能为空！";
    String WORKER_NAME_EMPTY = "【参数校验失败】姓名不能为空！";
    String WORKER_CODE_INVALID = "【参数校验失败】工号长度不能超过100个字符！";
    String WORKER_YEAR_NEGATIVE_INVALID = "【参数校验失败】工龄不能为负数！";
    String WORKER_YEAR_INVALID = "【参数校验失败】工龄不能超过100年！";
    String WORKER_NAME_INVALID = "【参数校验失败】姓名长度不能超过50个字符！";
    String WORKER_POSITION_INVALID = "【参数校验失败】岗位名称长度不能超过100个字符！";
    String WORKER_TYPE_EMPTY = "【参数校验失败】工种不能为空！";
    String WORKER_DELETE_BATCH_ID_EMPTY = "【参数校验失败】批量删除的工人ID列表不能为空！";
    String WORKER_STATUS_EMPTY = "【参数校验失败】工人状态不能为空！";
    String WORKER_TYPE_INVALID = "【参数校验失败】无效的工种！允许值：高空作业、受限空间、设备操作、正常作业！";
    String WORKER_STATUS_INVALID = "【参数校验失败】无效的状态！允许值：正常、异常、离线！";

    // ======================== 风险指标业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String RISK_HEART_RATE_EMPTY = "【参数校验失败】心率不能为空！";
    String RISK_HEART_RATE_INVALID = "【参数校验失败】心率值无效！需在1-300bpm范围内！";
    String RISK_MIN_HEART_RATE_INVALID = "【参数校验失败】心率最小值无效！需在1-300bpm范围内！";
    String RISK_MAX_HEART_RATE_INVALID = "【参数校验失败】心率最大值无效！需在1-300bpm范围内！";
    String RISK_RESPIRATORY_RATE_EMPTY = "【参数校验失败】呼吸率不能为空！";
    String RISK_RESPIRATORY_RATE_INVALID = "【参数校验失败】呼吸率值无效！需在1-60次/min范围内！";
    String RISK_MIN_RESPIRATORY_RATE_INVALID = "【参数校验失败】呼吸率最小值无效！需在1-60次/min范围内！";
    String RISK_MAX_RESPIRATORY_RATE_INVALID = "【参数校验失败】呼吸率最大值无效！需在1-60次/min范围内！";
    String RISK_FATIGUE_PERCENT_INVALID = "【参数校验失败】疲劳百分比无效！需在0～100%之间！";
    String RISK_MIN_FATIGUE_PERCENT_INVALID = "【参数校验失败】疲劳百分比最小值无效！需在0～100%之间！";
    String RISK_MAX_FATIGUE_PERCENT_INVALID = "【参数校验失败】疲劳百分比最大值无效！需在0～100%之间！";
    String RISK_FATIGUE_PERCENT_EMPTY = "【参数校验失败】疲劳百分比不能为空！";
    String RISK_CREATE_ERROR = "【风险指标操作失败】创建风险指标记录失败，请稍后重试！";
    String RISK_LEVEL_EMPTY = "【参数校验失败】风险等级不能为空！";
    String RISK_INDICATOR_ID_LIST_EMPTY = "【参数校验失败】风险指标ID列表不能为空！";
    String RISK_INDICATOR_NOT_EXIST = "【参数校验失败】风险指标记录不存在！";
    String RISK_LEVEL_INVALID = "【参数校验失败】无效的风险等级！允许值：低风险、中风险、高风险、严重风险！";
    String RISK_INDICATOR_DELETE_BATCH_ID_EMPTY = "【参数校验失败】批量删除的风险指标ID列表不能为空！";
    String RISK_INDICATOR_DELETE_BATCH_ID_INVALID = "【风险指标操作失败】批量删除风险指标失败，请检查ID合法性！";
    String RISK_RECORD_START_TIME_INVALID = "【参数校验失败】记录开始时间不能晚于当前时间！";
    String RISK_RECORD_END_TIME_INVALID = "【参数校验失败】记录结束时间不能晚于当前时间！";

    // ======================== 工作区域业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORK_AREA_RISK_LEVEL_EMPTY = "【参数校验失败】区域风险等级不能为空！";
    String WORK_AREA_CODE_EMPTY = "【参数校验失败】区域编码不能为空！";
    String WORK_AREA_NAME_EMPTY = "【参数校验失败】区域名称不能为空！";
    String WORK_AREA_NAME_INVALID = "【参数校验失败】区域名称长度不能超过100个字符！";
    String WORK_AREA_CODE_INVALID = "【参数校验失败】区域编码长度不能超过20个字符！";
    String WORK_AREA_DESCRIPTION_INVALID = "【参数校验失败】区域描述长度不能超过200个字符！";
    String WORK_AREA_CODE_DUPLICATE = "【参数校验失败】工作区域编码重复：%s，请更换编码！";
    String WORK_AREA_NOT_EXIST = "【参数校验失败】工作区域不存在，请确认区域ID是否正确！";
    String WORK_AREA_RISK_LEVEL_INVALID = "【参数校验失败】无效的区域风险等级！允许值：低风险、中风险、高风险！";
    String WORK_AREA_DELETE_BATCH_ID_EMPTY = "【参数校验失败】批量删除的区域ID列表不能为空！";
    String WORK_AREA_DELETE_BATCH_ID_INVALID = "【工作区域操作失败】批量删除区域失败，请检查ID合法性！";

    // ======================== 预警记录业务失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ALERT_RECORD_NOT_EXIST = "【参数校验失败】预警记录不存在！";
    String ALERT_RECORD_HANDLE_ERROR = "【预警记录操作失败】标记预警记录为已处理失败，请稍后重试！";
    String ALERT_LEVEL_EMPTY = "【参数校验失败】预警等级不能为空！";
    String ALERT_TYPE_EMPTY = "【参数校验失败】预警类型不能为空！";
    String ALERT_LEVEL_ID_LIST_EMPTY = "【参数校验失败】预警记录ID列表不能为空！";
    String ALERT_LEVEL_HANDLER_EMPTY = "【参数校验失败】预警记录处理人不能为空！";
    String ALERT_LEVEL_INVALID = "【参数校验失败】无效的预警等级！允许值：警告、严重！";
    String ALERT_RECORD_DELETE_BATCH_ID_EMPTY = "【参数校验失败】批量删除的预警记录ID列表不能为空！";
    String ALERT_RECORD_DELETE_BATCH_ID_INVALID = "【预警记录操作失败】批量删除预警记录失败，请检查ID合法性！";
    String ALERT_RECORD_MESSAGE_INVALID = "【参数校验失败】预警信息长度不能超过200个字符！";

    // ======================== 分页参数失败信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String PAGE_NUMBER_INVALID = "【参数校验失败】页码不能小于1！";
    String PAGE_SIZE_INVALID = "【参数校验失败】每页条数需在1-100之间！";

}