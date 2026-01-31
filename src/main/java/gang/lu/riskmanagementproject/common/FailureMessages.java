package gang.lu.riskmanagementproject.common;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 17:00
 * @description 失败信息集合
 */
public class FailureMessages {
    public static final String DATABASE_ERROR_MESSAGE = "数据库操作失败，请稍后重试";
    public static final String PARAMETER_VERIFY_ERROR_MESSAGE = "参数校验失败，请重试";
    public static final String REQUEST_PARAMETER_ERROR_MESSAGE = "请求参数错误，请重试";
    public static final String UNIVERSAL_ERROR_MESSAGE = "系统内部错误，请联系管理员";


    public static final String CREATE_WORKER_ERROR_MESSAGE = "创建工人失败！";
    public static final String WORKER_NOT_EXISTING_ERROR_MESSAGE = "工人不存在！";
    public static final String DUPLICATE_WORKER_ID_ERROR_MESSAGE = "工号不得重复！";
    public static final String WORKER_ID_NOT_EXISTING_ERROR_MESSAGE = "工号不能为空！";

    public static final String INVALID_ID_ERROR_MESSAGE = "无效的id！";
}
