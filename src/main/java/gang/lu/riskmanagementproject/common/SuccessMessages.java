package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 10:58
 * @description 操作成功的信息
 */
public interface SuccessMessages {

    // ======================== 预警记录相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ALERT_RECORD_ADD_SUCCESS = "新增预警记录成功！";
    String ALERT_RECORD_DELETE_SUCCESS = "删除预警记录成功！";
    String ALERT_RECORD_UPDATE_SUCCESS = "修改预警记录成功！";
    String ALERT_RECORD_GET_SUCCESS = "查询预警记录成功！";
    String ALERT_RECORD_MARK_HANDLED_SUCCESS = "标记预警记录已处理成功！";

    // ======================== 风险指标相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String RISK_INDICATOR_ADD_SUCCESS = "新增风险指标成功！";
    String RISK_INDICATOR_GET_LATEST_SUCCESS = "查询最新风险指标成功！";
    String RISK_INDICATOR_GET_HISTORY_SUCCESS = "查询历史风险指标成功，共%s条记录！";
    String RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS = "统计风险等级人数分布成功！";
    String RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS = "统计时间段高风险工人数成功！";

    // ======================== 工作区域相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORK_AREA_ADD_SUCCESS = "新增工作区域成功！";
    String WORK_AREA_DELETE_SUCCESS = "删除工作区域成功！";
    String WORK_AREA_UPDATE_SUCCESS = "修改工作区域成功！";
    String WORK_AREA_GET_SUCCESS = "查询工作区域成功！";
    String WORK_AREA_GET_BY_CODE_SUCCESS = "按编码查询工作区域成功，共%s条记录！";
    String WORK_AREA_GET_ALL_BY_PAGE_CONDITIONAL_SUCCESS = "多条件分页查询工作区域成功！";
    String WORK_AREA_GET_ALL_BY_PAGE_SUCCESS = "分页查询所有工作区域成功！";
    String WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS = "统计工作区域风险等级数量成功！";

    // ======================== 工人管理相关成功信息 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String WORKER_CREATE_SUCCESS = "创建工人信息成功！";
    String WORKER_DELETE_SUCCESS = "删除工人信息成功！";
    String WORKER_DELETE_BATCH_SUCCESS = "批量删除工人成功！";
    String WORKER_UPDATE_SUCCESS = "更新工人信息成功！";
    String WORKER_GET_SUCCESS = "查询工人信息成功！";
    String WORKER_GET_COUNT_SUCCESS = "查询工人信息成功！查询到%s条工人信息！";
    String WORKER_GET_COUNT_BY_NAME_SUCCESS = "按姓名查询工人成功，共%s条记录！";
    String WORKER_GET_COUNT_BY_STATUS_SUCCESS = "按状态[%s]查询工人成功，共%s条记录！";
    String WORKER_GET_COUNT_BY_POSITION_SUCCESS = "按岗位[%s]查询工人成功，共%s条记录！";
    String WORKER_GET_COUNT_BY_WORKTYPE_SUCCESS = "按工种[%s]查询工人成功，共%s条记录！";
    String WORKER_GET_BY_CONDITION_SUCCESS = "组合条件查询工人成功，共%s条记录！";
    String WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS = "按状态统计工人数量成功！";
    String WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS = "按工种统计工人数量成功！";
}