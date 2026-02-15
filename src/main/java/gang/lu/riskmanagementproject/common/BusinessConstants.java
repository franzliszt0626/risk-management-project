package gang.lu.riskmanagementproject.common;

/**
 * <p>
 * 业务常量集合
 * </p>
 * 存储系统所有业务场景、字段名称、操作类型等常量
 *
 * @author Franz Liszt
 * @since 2026-02-11
 */
public interface BusinessConstants {
    // ======================== 通用字段常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ID = "id";
    String ID_LIST = "ID列表";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";

    // ======================== 预警记录相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_ALERT_RECORD = "新增预警记录";
    String UPDATE_ALERT_RECORD = "修改预警记录";
    String GET_ALERT_RECORD = "查询预警记录";
    String GET_ALERT_RECORD_BY_MULTIPLY_CONDITION = "多条件查询预警记录";
    String HANDLE_ALERT_RECORD = "标记预警记录已处理";
    String DELETE_ALERT_RECORD = "删除预警记录";
    String BATCH_DELETE_ALERT_RECORD = "批量删除预警记录";

    String ALERT_RECORD_ID = "预警记录ID";
    String ALERT_LEVEL = "预警等级";
    String ALERT_TYPE = "预警类型";
    String HANDLED_BY = "处理人";

    // ======================== 风险指标相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_RISK_INDICATOR = "新增风险指标";
    String UPDATE_RISK_INDICATOR = "修改风险指标";
    String GET_LATEST_RISK_INDICATOR = "查询最新风险指标";
    String DELETE_RISK_INDICATOR = "删除风险指标";
    String BATCH_DELETE_RISK_INDICATOR = "批量删除风险指标";
    String GET_RISK_INDICATOR_BY_MULTIPLY_CONDITION = "多条件查询风险指标";
    String GET_RISK_INDICATOR = "查询风险指标";
    String GET_RISK_LEVEL_DISTRIBUTION = "统计风险等级人数分布";
    String GET_HIGH_RISK_DISTRIBUTION_IN_PERIOD = "统计时间段高风险人数";
    String RISK_INDICATOR_ID = "风险指标ID";
    String RISK_LEVEL = "风险等级";
    String HEART_RATE = "心率";
    String RESPIRATORY_RATE = "呼吸率";
    String FATIGUE_PERCENT = "疲劳百分比";

    // ======================== 工作区域相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_WORK_AREA = "新增工作区域";
    String UPDATE_WORK_AREA = "修改工作区域";
    String BATCH_DELETE_WORK_AREA = "批量删除工作区域";
    String DELETE_WORK_AREA = "删除工作区域";
    String GET_WORK_AREA = "查询工作区域";
    String GET_WORK_AREA_BY_MULTIPLY_CONDITION = "多条件查询工作区域";
    String GET_WORK_AREA_BY_CODE = "按编码查询工作区域";
    String GET_WORK_AREA_BY_ID = "按ID查询工作区域";
    String GET_WORK_AREA_DISTRIBUTION_BY_RISK_LEVEL = "统计工作区域风险等级数量";
    String WORK_AREA_ID = "工作区域ID";
    String AREA_RISK_LEVEL = "区域风险等级";
    String WORK_AREA_CODE = "区域编码";
    String WORK_AREA_NAME = "区域名称";

    // ======================== 工人相关常量 ========================
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    String ADD_WORKER = "新增工人";
    String UPDATE_WORKER = "修改工人";
    String GET_WORKER = "查询工人";
    String GET_WORKER_BY_MULTIPLY_CONDITION = "多条件查询工人";
    String DELETE_WORKER = "删除工人";
    String BATCH_DELETE_WORKER = "批量删除工人";
    String GET_WORKER_BY_WORKCODE = "按工号查询工人";
    String GET_WORKER_BY_ID = "按ID查询工人";
    String GET_WORKER_DISTRIBUTION_BY_STATUS = "统计工人状态分布";
    String GET_WORKER_DISTRIBUTION_BY_WORKTYPE = "统计工人工种分布";

    String WORKER_ID = "工人ID";
    String WORK_TYPE = "工种";
    String WORK_YEAR = "工龄";
    String STATUS = "工人状态";
    String WORKER_CODE = "工号";
    String NAME = "姓名";
    String WORKER_STATUS = "status";
    String WORKER_TYPE = "work_type";

    // ======================== 校验相关常量 ========================

    String VALIDATE_STRING_NO_EMPTY = "字符串非空校验";
    String VALIDATE_ENUM_NO_EMPTY = "枚举非空校验";
    String VALIDATE_BATCH_ID_EXIST = "批量ID存在性校验";
    String VALIDATE_SINGLE_ID_EXIST = "单ID存在性校验";
}