package gang.lu.riskmanagementproject.util;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 13:25
 * @description 业务校验
 */
@Slf4j
@Component
public class BusinessVerifyUtil {

    private static WorkerMapper workerMapper;

    public BusinessVerifyUtil(WorkerMapper workerMapper) {
        BusinessVerifyUtil.workerMapper = workerMapper;
    }

    /**
     * 校验ID合法性（非空、非负）
     *
     * @param id     待校验的ID
     * @param idName ID名称（如"工人ID"、"风险指标ID"），用于日志/异常提示
     */
    public static void validateId(Long id, String idName) {
        if (ObjectUtil.isNull(id) || id <= 0) {
            String errorMsg = String.format("【%s不合法】传入值：%s", idName, id);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.COMMON_INVALID_ID_ERROR);
        }
    }

    /**
     * 校验工人ID合法性 + 工人存在性
     *
     * @param workerId      工人ID
     * @param businessScene 业务场景（如"新增风险指标"、"查询最新风险指标"）
     */
    public static void validateWorker(Long workerId, String businessScene) {
        // 1. 先校验ID格式
        validateId(workerId, "工人ID");

        // 2. 校验工人是否存在
        if (ObjectUtil.isNull(workerMapper.selectById(workerId))) {
            // 3. 不存在的话返回失败信息：工人不存在
            String errorMsg = String.format(
                    "【%s失败】%s",
                    businessScene,
                    String.format(FailureMessages.RISK_DATA_WORKER_NOT_EXIST, workerId)
            );
            log.warn(errorMsg);
            throw new BizException(String.format(FailureMessages.RISK_DATA_WORKER_NOT_EXIST, workerId));
        }
    }

    /**
     * 从Map中安全获取count值（处理null、类型转换）
     *
     * @param dataMap 原始Map
     * @param key     键名
     * @return 转换后的Integer值（默认0）
     */
    public static Integer getCountFromMap(Map<String, Map<String, Object>> dataMap, String key) {
        if (ObjectUtil.isEmpty(dataMap) || !dataMap.containsKey(key)) {
            return 0;
        }
        Map<String, Object> subMap = dataMap.get(key);
        if (ObjectUtil.isEmpty(subMap) || !subMap.containsKey("count")) {
            return 0;
        }
        // 安全转换：先转Number再转Integer，兼容Long/Integer类型
        Number countNum = (Number) subMap.get("count");
        return ObjectUtil.isNull(countNum) ? 0 : countNum.intValue();
    }

    /**
     * 处理分页参数（默认值 + 范围校验）
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 处理后的分页参数数组 [pageNum, pageSize]
     */
    public static Integer[] handlePageParams(Integer pageNum, Integer pageSize) {
        // 默认值
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, 10);

        // 范围校验
        finalPageNum = finalPageNum <= 0 ? 1 : finalPageNum;
        finalPageSize = (finalPageSize <= 0 || finalPageSize > 100) ? 100 : finalPageSize;

        log.info("分页参数处理完成：原始[{}，{}] → 处理后[{}，{}]", pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }

    // ========== 新增：风险等级通用校验 ==========

    /**
     * 校验风险等级合法性（非空+有效枚举值）
     *
     * @param riskLevel     风险等级枚举
     * @param businessScene 业务场景（如"新增风险指标"）
     */
    public static void validateRiskLevel(RiskLevel riskLevel, String businessScene) {
        // 1. 空值校验
        if (ObjectUtil.isNull(riskLevel)) {
            String errorMsg = String.format("【%s失败】风险等级为空", businessScene);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.RISK_PARAM_EMPTY_RISK_LEVEL);
        }

        // 2. 枚举值有效性校验（兜底，防止枚举转换异常）
        try {
            RiskLevel.fromValue(riskLevel.getValue());
        } catch (IllegalArgumentException e) {
            String errorMsg = String.format("【%s失败】无效的风险等级：%s", businessScene, riskLevel.getValue());
            log.warn(errorMsg, e);
            throw new BizException(String.format(FailureMessages.RISK_PARAM_INVALID_RISK_LEVEL, riskLevel.getValue()));
        }
    }

    /**
     * 安全转换Long为Integer（避免空指针/类型转换异常）
     *
     * @param value        待转换的Long值
     * @param defaultValue 默认值（null时返回）
     */
    public static Integer safeLongToInt(Long value, Integer defaultValue) {
        return ObjectUtil.isNull(value) ? defaultValue : value.intValue();
    }

    // ========== 工作区域专用（新增AreaRiskLevel相关） ==========

    /**
     * 校验工作区域编码合法性（非空+非空白）
     *
     * @param areaCode      区域编码
     * @param businessScene 业务场景
     */
    public static void validateWorkAreaCode(String areaCode, String businessScene) {
        if (StrUtil.isBlank(areaCode)) {
            String errorMsg = String.format("【%s失败】工作区域编号不能为空", businessScene);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.WORK_AREA_PARAM_EMPTY_CODE);
        }
    }

    /**
     * 校验工作区域名称合法性（非空+非空白）
     *
     * @param areaName      区域名称
     * @param businessScene 业务场景
     */
    public static void validateWorkAreaName(String areaName, String businessScene) {
        if (StrUtil.isBlank(areaName)) {
            String errorMsg = String.format("【%s失败】工作区域名称不能为空", businessScene);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.WORK_AREA_PARAM_EMPTY_NAME);
        }
    }

    /**
     * 校验AreaRiskLevel合法性（非空+有效枚举值）
     *
     * @param riskLevel     工作区域风险等级枚举
     * @param businessScene 业务场景
     */
    public static void validateAreaRiskLevel(AreaRiskLevel riskLevel, String businessScene) {
        // 1. 空值校验
        if (ObjectUtil.isNull(riskLevel)) {
            String errorMsg = String.format("【%s失败】工作区域风险等级为空", businessScene);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.RISK_PARAM_EMPTY_RISK_LEVEL);
        }

        // 2. 枚举值有效性校验（兜底）
        try {
            AreaRiskLevel.fromValue(riskLevel.getValue());
        } catch (IllegalArgumentException e) {
            String errorMsg = String.format("【%s失败】无效的工作区域风险等级：%s", businessScene, riskLevel.getValue());
            log.warn(errorMsg, e);
            throw new BizException(String.format(FailureMessages.RISK_PARAM_INVALID_RISK_LEVEL, riskLevel.getValue()));
        }
    }

    /**
     * 安全转换AreaRiskLevel枚举为数据库存储的字符串
     *
     * @param riskLevel 工作区域风险等级枚举
     * @return 风险等级字符串（null返回null）
     */
    public static String convertAreaRiskLevelToString(AreaRiskLevel riskLevel) {
        return ObjectUtil.isNull(riskLevel) ? null : riskLevel.getValue();
    }

    /**
     * 校验工作区域分页参数（默认20，最大50）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 处理后的分页参数 [pageNum, pageSize]
     */
    public static Integer[] handleWorkAreaPageParams(Integer pageNum, Integer pageSize) {
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, 20);
        finalPageSize = Math.min(finalPageSize, 50);
        log.info("工作区域分页参数处理完成：原始[{}，{}] → 处理后[{}，{}]", pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }
    // ========== 工人专用校验（新增核心） ==========

    /**
     * 校验工号合法性（非空+非空白）
     */
    public static void validateWorkerCode(String workerCode, String businessScene) {
        if (StrUtil.isBlank(workerCode)) {
            String errorMsg = String.format("【%s失败】工号不能为空", businessScene);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.WORKER_PARAM_EMPTY_CODE);
        }
    }

    /**
     * 校验工号唯一性（排除自身ID）
     */
    public static void validateWorkerCodeUnique(String workerCode, Long excludeId, String businessScene) {
        validateWorkerCode(workerCode, businessScene);
        boolean exists = ObjectUtil.isNotNull(workerMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<gang.lu.riskmanagementproject.domain.po.Worker>()
                        .eq(gang.lu.riskmanagementproject.domain.po.Worker::getWorkerCode, workerCode)
                        .ne(ObjectUtil.isNotNull(excludeId), gang.lu.riskmanagementproject.domain.po.Worker::getId, excludeId)
        ));
        if (exists) {
            String errorMsg = String.format("【%s失败】工号重复：%s", businessScene, workerCode);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.WORKER_PARAM_DUPLICATE_CODE);
        }
    }

    /**
     * 校验Status枚举合法性（非空+有效）
     */
    public static void validateStatus(Status status, String businessScene) {
        if (ObjectUtil.isNull(status)) {
            String errorMsg = String.format("【%s失败】工人状态为空", businessScene);
            log.warn(errorMsg);
            throw new BizException("工人状态不能为空");
        }
        try {
            Status.fromValue(status.getValue());
        } catch (IllegalArgumentException e) {
            String errorMsg = String.format("【%s失败】无效的工人状态：%s", businessScene, status.getValue());
            log.warn(errorMsg, e);
            throw new BizException(errorMsg);
        }
    }

    /**
     * 校验WorkType枚举合法性（非空+有效）
     */
    public static void validateWorkType(WorkType workType, String businessScene) {
        if (ObjectUtil.isNull(workType)) {
            String errorMsg = String.format("【%s失败】工种为空", businessScene);
            log.warn(errorMsg);
            throw new BizException("工种不能为空");
        }
        try {
            WorkType.fromValue(workType.getValue());
        } catch (IllegalArgumentException e) {
            String errorMsg = String.format("【%s失败】无效的工种：%s", businessScene, workType.getValue());
            log.warn(errorMsg, e);
            throw new BizException(errorMsg);
        }
    }

    /**
     * 校验工人存在性（通过ID）
     */
    public static void validateWorkerExists(Long workerId, String businessScene) {
        validateId(workerId, "工人ID");
        if (ObjectUtil.isNull(workerMapper.selectById(workerId))) {
            String errorMsg = String.format("【%s失败】工人不存在，ID：%s", businessScene, workerId);
            log.warn(errorMsg);
            throw new BizException(FailureMessages.WORKER_DATA_NOT_EXIST);
        }

    }
}