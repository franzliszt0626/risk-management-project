package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 10:58
 * @description 操作成功的信息
 */
public class SuccessMessages {
    /**
     * 预警记录相关成功信息
     */
    public static final String ALERT_RECORD_ADD_SUCCESS_MESSAGE = "新增预警记录成功！";
    public static final String ALERT_RECORD_DELETE_SUCCESS_MESSAGE = "删除预警记录成功！";
    public static final String ALERT_RECORD_UPDATE_SUCCESS_MESSAGE = "修改预警记录成功！";
    public static final String ALERT_RECORD_GET_SUCCESS_MESSAGE = "查询预警记录成功！";
    public static final String ALERT_RECORD_MARK_HANDLED_SUCCESS_MESSAGE = "标记成功！";

    /**
     * 风险指标相关接口成功信息
     */
    public static final String RISK_INDICATOR_ADD_SUCCESS_MESSAGE = "新增风险记录成功！";
    public static final String RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS_MESSAGE = "统计风险等级人数分布成功！";
    public static final String RISK_INDICATOR_GET_HISTORY_SUCCESS_MESSAGE = "查询历史风险指标成功，共%s条记录！";
    public static final String RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS_MESSAGE = "统计时间段高风险人数成功！";
    public static final String RISK_INDICATOR_GET_LATEST_SUCCESS_MESSAGE = "查询最新风险指标成功！";

    /**
     * 工作区域相关成功信息
     */
    public static final String WORK_AREA_GET_LATEST_SUCCESS_MESSAGE = "查询工作区域成功！";
    public static final String WORK_AREA_GET_BY_CODE_SUCCESS_MESSAGE = "按编码查询工作区域成功，共%s条记录！";
    public static final String WORK_AREA_GET_ALL_BY_PAGE_CONDITIONAL_SUCCESS_MESSAGE = "多条件分页查询工作区域成功！";
    public static final String WORK_AREA_GET_ALL_BY_PAGE_SUCCESS_MESSAGE = "分页查询所有工作区域成功！";
    public static final String WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS_MESSAGE = "统计工作区域风险等级数量成功！";
    public static final String WORK_AREA_UPDATE_SUCCESS_MESSAGE = "修改工作区域成功！";
    public static final String WORK_AREA_DELETE_SUCCESS_MESSAGE = "删除工作区域成功！";
    public static final String WORK_AREA_ADD_SUCCESS_MESSAGE = "新增工作区域成功！";


}
