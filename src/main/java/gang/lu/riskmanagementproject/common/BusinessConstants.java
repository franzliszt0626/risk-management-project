package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:50
 * @description 业务场景
 */
public interface BusinessConstants {
    // ======================== 通用字段常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ID = "id";
    String VALUE = "value";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";

    // ======================== 预警记录相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_ALERT_RECORD = "新增预警记录";
    String UPDATE_ALERT_RECORD = "修改预警记录";
    String GET_ALERT_RECORD = "查询工人预警记录";
    String HANDLE_ALERT_RECORD = "标记预警记录已处理";
    String DELETE_ALERT_RECORD = "删除预警记录";

    String ALERT_RECORD_ID = "预警记录ID";
    String ALERT_LEVEL = "预警等级";
    String ALERT_TYPE = "预警类型";
    String HANDLED_BY = "处理人";

    // ======================== 风险指标相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_RISK_INDICATOR = "新增风险指标";
    String GET_LATEST_RISK_INDICATOR = "查询最新风险指标";
    String GET_HISTORY_RISK_INDICATOR = "查询历史风险指标";
    String GET_RISK_INDICATOR = "查询风险指标";
    String GET_HISTORY_RISK_INDICATOR_BY_WORKER_ID = "根据工人id查询历史风险指标";

    String RISK_LEVEL = "工人风险等级";
    String HEART_RATE = "心率";
    String RESPIRATORY_RATE = "呼吸率";
    String FATIGUE_PERCENT = "疲劳百分比";

    // ======================== 工作区域相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_WORK_AREA = "新增工作区域";
    String UPDATE_WORK_AREA = "修改工作区域";
    String DELETE_WORK_AREA = "删除工作区域";
    String GET_WORK_AREA = "查询工作区域";
    String GET_WORK_AREA_BY_CODE = "按编码查询工作区域";
    String GET_WORK_AREA_BY_ID = "按ID查询工作区域";

    String WORK_AREA_ID = "工作区域ID";
    String AREA_RISK_LEVEL = "区域风险等级";
    String WORK_AREA_CODE = "区域编码";
    String WORK_AREA_NAME = "区域名称";

    // ======================== 工人相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_WORKER = "创建工人";
    String UPDATE_WORKER = "更新工人";
    String GET_WORKER = "查询工人";
    String DELETE_WORKER = "删除工人";
    String BATCH_DELETE_WORKER = "批量删除工人";
    String GET_WORKER_BY_WORKCODE = "根据工号查询工人";
    String GET_WORKER_BY_STATUS = "根据状态查询工人";
    String GET_WORKER_BY_WORKTYPE = "按工种查询工人";

    String WORKER_ID = "工人ID";
    String WORK_TYPE = "工种";
    String STATUS = "工人状态";
    String WORKER_CODE = "工人工号";
    String NAME = "工人姓名";
    String WORKER_STATUS = "status";
    String WORKER_TYPE = "work_type";
}

