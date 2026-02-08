package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 统一业务校验工具类
 * 职责：所有业务层的参数校验、存在性校验、分页参数处理
 *
 * @author FranzLiszt
 */
@Slf4j
@Component
public class BusinessVerifyUtil {


    private static WorkerMapper workerMapper;

    public BusinessVerifyUtil(WorkerMapper workerMapper) {
        BusinessVerifyUtil.workerMapper = workerMapper;
    }

    // ========== 通用基础校验 ==========

    /**
     * 通用ID校验（非空、非负）
     */
    public static void validateId(Long id, String idName) {
        if (ObjectUtil.isNull(id) || id <= 0) {
            String errorMsg = String.format("【%s不合法】传入值：%s", idName, id);
            log.warn(errorMsg);
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_INVALID_ID_ERROR);
        }
    }

    /**
     * 通用字符串非空校验
     */
    public static void validateStringNotBlank(String value, String fieldName, String businessScene) {
        if (StrUtil.isBlank(value)) {
            String errorMsg = String.format("【%s失败】%s不能为空", businessScene, fieldName);
            log.warn(errorMsg);
            throw new BizException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }

    /**
     * 通用枚举非空校验
     */
    public static <E> void validateEnumNotNull(E enumValue, String enumName, String businessScene) {
        if (ObjectUtil.isNull(enumValue)) {
            String errorMsg = String.format("【%s失败】%s不能为空", businessScene, enumName);
            log.warn(errorMsg);
            throw new BizException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }

    /**
     * 通用分页参数处理（默认10条/页，最大100）
     */
    public static Integer[] handleCommonPageParams(Integer pageNum, Integer pageSize) {
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, 10);
        finalPageNum = Math.max(finalPageNum, 1);
        finalPageSize = Math.max(1, Math.min(finalPageSize, 100));
        log.info("通用分页参数处理完成：原始[{}，{}] → 处理后[{}，{}]", pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }

    // ========== 工人相关校验（合并重复逻辑） ==========

    /**
     * 校验工人ID合法性+存在性（合并原validateWorker/validateWorkerExists）
     */
    public static void validateWorkerExist(Long workerId, String businessScene) {
        validateId(workerId, "工人ID");
        if (ObjectUtil.isNull(workerMapper.selectById(workerId))) {
            String errorMsg = String.format("【%s失败】工人不存在，ID：%s", businessScene, workerId);
            log.warn(errorMsg);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
    }

    /**
     * 工号校验（非空+唯一性）
     */
    public static void validateWorkerCode(String workerCode, Long excludeId, String businessScene) {
        // 1. 非空校验
        validateStringNotBlank(workerCode, "工号", businessScene);
        // 2. 唯一性校验（excludeId=null表示新增）
        boolean exists = ObjectUtil.isNotNull(workerMapper.selectOne(
                new LambdaQueryWrapper<Worker>()
                        .eq(Worker::getWorkerCode, workerCode)
                        .ne(ObjectUtil.isNotNull(excludeId), Worker::getId, excludeId)
        ));
        if (exists) {
            String errorMsg = String.format("【%s失败】工号重复：%s", businessScene, workerCode);
            log.warn(errorMsg);
            throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORKER_PARAM_DUPLICATE_CODE);
        }
    }

    /**
     * 工人分页参数处理（默认20条/页，最大50）
     */
    public static Integer[] handleWorkerPageParams(Integer pageNum, Integer pageSize) {
        return handleCustomPageParams(pageNum, pageSize, 20, 50, "工人");
    }

    // ========== 工作区域相关校验 ==========

    /**
     * 工作区域编码/名称非空校验
     */
    public static void validateWorkAreaField(String value, String fieldName, String businessScene) {
        validateStringNotBlank(value, fieldName, businessScene);
    }

    /**
     * 工作区域分页参数处理（默认20条/页，最大50）
     */
    public static Integer[] handleWorkAreaPageParams(Integer pageNum, Integer pageSize) {
        return handleCustomPageParams(pageNum, pageSize, 20, 50, "工作区域");
    }

    // ========== 风险等级相关校验（合并重复逻辑） ==========

    /**
     * 通用风险等级枚举校验（兼容RiskLevel/AreaRiskLevel）
     */
    public static <E> void validateRiskLevel(E riskLevel, String businessScene) {
        validateEnumNotNull(riskLevel, "风险等级", businessScene);
        // 枚举本身已保证合法性，无需冗余校验
    }

    // ========== 预警记录相关校验 ==========

    /**
     * 预警等级非空校验
     */
    public static void validateAlertLevel(Object alertLevel, String businessScene) {
        validateEnumNotNull(alertLevel, "预警等级", businessScene);
    }

    // ========== 通用工具方法 ==========

    /**
     * 自定义分页参数处理（复用逻辑）
     */
    private static Integer[] handleCustomPageParams(Integer pageNum, Integer pageSize, int defaultSize, int maxSize, String bizType) {
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, defaultSize);
        finalPageNum = Math.max(finalPageNum, 1);
        finalPageSize = Math.max(1, Math.min(finalPageSize, maxSize));
        log.info("{}分页参数处理完成：原始[{}，{}] → 处理后[{}，{}]", bizType, pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }

    /**
     * 从Map中安全获取count值（统一处理类型转换）
     */
    public static Integer getCountFromMap(Map<String, Map<String, Object>> dataMap, String key) {
        if (ObjectUtil.isEmpty(dataMap) || !dataMap.containsKey(key)) {
            return 0;
        }
        Map<String, Object> subMap = dataMap.get(key);
        if (ObjectUtil.isEmpty(subMap) || !subMap.containsKey("count")) {
            return 0;
        }
        Number countNum = (Number) subMap.get("count");
        return ObjectUtil.isNull(countNum) ? 0 : countNum.intValue();
    }

    /**
     * 安全转换Long为Integer
     */
    public static Integer safeLongToInt(Long value, Integer defaultValue) {
        return ObjectUtil.isNull(value) ? defaultValue : value.intValue();
    }

    /**
     * 获取时间段描述（从RiskIndicatorImpl中抽取）
     */
    public static String getPeriodDesc(Integer period) {
        return switch (period) {
            case 0 -> "00:00-04:00";
            case 4 -> "04:00-08:00";
            case 8 -> "08:00-12:00";
            case 12 -> "12:00-16:00";
            case 16 -> "16:00-20:00";
            case 20 -> "20:00-24:00";
            default -> "未知时间段";
        };
    }
}