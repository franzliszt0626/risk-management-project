package gang.lu.riskmanagementproject.common;

/**
 * <p>
 * 系统操作成功提示信息常量
 * </p>
 * 统一管理各模块操作成功的提示文案，保证格式、风格一致，便于前端统一展示
 *
 * @author Franz Liszt
 * @since 2026-02-08
 */
public interface SuccessMessages {

    // ======================== 预警记录相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ALERT_RECORD_ADD_SUCCESS = "新增预警记录成功！";
    String ALERT_RECORD_DELETE_SUCCESS = "删除预警记录成功！";
    String ALERT_RECORD_BATCH_DELETE_SUCCESS = "批量删除预警记录成功！";
    String ALERT_RECORD_UPDATE_SUCCESS = "修改预警记录成功！";
    String ALERT_RECORD_GET_SUCCESS = "查询预警记录成功！";
    String ALERT_RECORD_GET_COUNT_SUCCESS = "查询预警记录成功，共%s条！";
    String ALERT_RECORD_MARK_HANDLED_SUCCESS = "标记预警记录为已处理成功！";

    // ======================== 风险指标相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String RISK_INDICATOR_ADD_SUCCESS = "新增风险指标成功！";
    String RISK_INDICATOR_DELETE_SUCCESS = "删除风险指标成功！";
    String RISK_INDICATOR_BATCH_DELETE_SUCCESS = "批量删除风险指标成功！";
    String RISK_INDICATOR_UPDATE_SUCCESS = "修改风险指标成功！";
    String RISK_INDICATOR_GET_SUCCESS = "查询风险指标成功！";
    String RISK_INDICATOR_GET_LATEST_SUCCESS = "查询工人最新风险指标成功！";
    String RISK_INDICATOR_GET_COUNT_SUCCESS = "查询风险指标记录成功，共%s条！";
    String RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS = "统计工人风险等级分布成功！";
    String RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS = "统计当日高风险工人时段分布成功！";

    // ======================== 工作区域相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORK_AREA_ADD_SUCCESS = "新增工作区域成功！";
    String WORK_AREA_DELETE_SUCCESS = "删除工作区域成功！";
    String WORK_AREA_DELETE_BATCH_SUCCESS = "批量删除工作区域成功！";
    String WORK_AREA_UPDATE_SUCCESS = "修改工作区域成功！";
    String WORK_AREA_GET_SUCCESS = "查询工作区域成功！";
    String WORK_AREA_GET_BY_CODE_SUCCESS = "按编码查询工作区域成功，共%s条！";
    String WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS = "统计工作区域风险等级分布成功！";
    String WORK_AREA_GET_COUNT_SUCCESS = "查询工作区域信息成功，共%s条！";
    // ======================== 工人管理相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORKER_CREATE_SUCCESS = "新增工人信息成功！";
    String WORKER_DELETE_SUCCESS = "删除工人信息成功！";
    String WORKER_DELETE_BATCH_SUCCESS = "批量删除工人信息成功！";
    String WORKER_UPDATE_SUCCESS = "修改工人信息成功！";
    String WORKER_GET_SUCCESS = "查询工人信息成功！";
    String WORKER_GET_COUNT_SUCCESS = "查询工人信息成功，共%s条！";
    String WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS = "统计工人状态分布成功！";
    String WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS = "统计工人工种分布成功！";
}