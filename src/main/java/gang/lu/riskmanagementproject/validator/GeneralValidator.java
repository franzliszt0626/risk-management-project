package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.common.FailedMessages.*;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-09 19:47:23
 * @description 统一校验器
 */
@Component
public class GeneralValidator {


    /**
     * 字符串非空校验
     */
    @ValidateLog(value = VALIDATE_STRING_NO_EMPTY, logLevel = ValidateLog.LogLevel.DEBUG)
    public void validateStringNotBlank(String value, String fieldName, String businessScene) {
        if (StrUtil.isBlank(value)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, fieldName));
        }
    }

    /**
     * 枚举非空校验
     */
    @ValidateLog(value = VALIDATE_ENUM_NO_EMPTY, logLevel = ValidateLog.LogLevel.DEBUG)
    public <E> void validateEnumNotNull(E enumValue, String enumName, String businessScene) {
        if (ObjectUtil.isNull(enumValue)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, enumName));
        }
    }

    /**
     * 通用数据库校验
     */
    public void validateDbOperateResult(int rows) {
        if (rows != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
        }
    }

    /**
     * 通用数据库操作结果校验（批量操作）
     *
     * @param rows          影响行数
     * @param expectMinRows 期望最小行数（一般传批量操作的ID数量）
     */
    public void validateBatchDbOperateResult(int rows, int expectMinRows) {
        // 批量操作允许影响行数 < 期望数（比如部分ID不存在），但不能为0
        if (rows <= 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
        }
    }

    /**
     * 通用批量ID存在性校验（抽取共性，所有业务复用）
     */
    @ValidateLog(value = VALIDATE_BATCH_ID_EXIST, logLevel = ValidateLog.LogLevel.WARN)
    public <T> void validateBatchIdsExist(List<Long> ids, BaseMapper<T> mapper, String notExistMsg) {
        if (CollUtil.isEmpty(ids)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, ID_LIST));
        }
        List<T> existList = mapper.selectBatchIds(ids);
        if (existList.size() != ids.size()) {
            Set<Long> existIds = existList.stream()
                    .map(item -> (Long) ReflectUtil.getFieldValue(item, ID))
                    .collect(Collectors.toSet());
            List<Long> notExistIds = ids.stream().filter(id -> !existIds.contains(id))
                    .collect(Collectors.toList());
            throw new BizException(HttpStatus.NOT_FOUND, String.format(notExistMsg, notExistIds));
        }
    }

    /**
     * 通用单ID存在性校验（仅校验数据是否存在，格式校验已由@ValidId完成）
     */
    @ValidateLog(value = VALIDATE_SINGLE_ID_EXIST, logLevel = ValidateLog.LogLevel.DEBUG)
    public <T> T validateIdExist(Long id, BaseMapper<T> mapper, String notExistMsg) {
        T entity = mapper.selectById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException(HttpStatus.NOT_FOUND, notExistMsg);
        }
        return entity;
    }

    /**
     * 时间校验
     */
    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new BizException(HttpStatus.BAD_REQUEST, COMMON_TIME_INVALID_ERROR);
        }
    }

    /**
     * 通用的 最小值 ≤ 最大值 校验方法（适配Integer/Double）
     *
     * @param min    最小值
     * @param max    最大值
     * @param bizKey 哪个字段
     */
    public <T extends Comparable<T>> void validateMinMaxRange(T min, T max, String bizKey) {
        // 只有当最小值和最大值都不为空时，才需要校验大小关系
        if (ObjectUtil.isNotNull(min) && ObjectUtil.isNotNull(max)) {
            if (min.compareTo(max) > 0) {
                throw new BizException(HttpStatus.BAD_REQUEST,
                        String.format(COMMON_MIN_MAX_INVALID_ERROR, bizKey));
            }
        }
    }

}