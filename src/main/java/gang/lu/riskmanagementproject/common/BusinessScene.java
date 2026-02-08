package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 19:49
 * @description 业务场景
 */
public class BusinessScene {
    public static final String ADD_ALERT_RECORD = "新增预警记录";
    public static final String UPDATE_ALERT_RECORD = "修改预警记录";
    public static final String GET_ALERT_RECORD = "查询工人预警记录";


    public static final String ADD_RISK_INDICATOR = "新增风险指标";
    public static final String GET_LATEST_RISK_INDICATOR = "查询最新风险指标";
    public static final String GET_HISTORY_RISK_INDICATOR = "查询历史风险指标";


    public static final String ADD_WORK_AREA = "新增工作区域";
    public static final String GET_WORK_AREA_BY_CODE = "按编码查询工作区域";

    public static final String ADD_WORKER = "创建工人";
    public static final String UPDATE_WORKER = "更新工人";
    public static final String GET_WORKER_BY_WORKCODE = "根据工号查询工人";
    public static final String GET_WORKER_BY_STATUS = "根据状态查询工人";
    public static final String GET_WORKER_BY_WORKTYPE = "按工种查询工人";
}
