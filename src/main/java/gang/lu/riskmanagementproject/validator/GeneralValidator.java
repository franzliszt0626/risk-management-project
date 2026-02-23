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

import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.*;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.ID_LIST;
import static gang.lu.riskmanagementproject.common.field.FieldEnglishConstants.ID;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 通用业务校验器
 * <p>
 * 封装各模块共用的参数合法性、数据存在性、时间范围等校验逻辑，
 * 统一抛出 {@link BizException}，由全局异常处理器统一响应。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Component
public class GeneralValidator {

    /**
     * 字符串非空校验。
     *
     * @param value         被校验的字符串
     * @param fieldName     字段中文名称（用于错误提示）
     * @param businessScene 业务场景名称（用于日志追踪）
     * @throws BizException 字符串为空时抛出 400 异常
     */
    @ValidateLog(value = VALIDATE_STRING_NO_EMPTY, logLevel = ValidateLog.LogLevel.DEBUG)
    public void validateStringNotBlank(String value, String fieldName, String businessScene) {
        if (StrUtil.isBlank(value)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, fieldName));
        }
    }

    /**
     * 枚举非空校验。
     *
     * @param enumValue     被校验的枚举值
     * @param enumName      枚举中文名称（用于错误提示）
     * @param businessScene 业务场景名称（用于日志追踪）
     * @param <E>           枚举类型
     * @throws BizException 枚举值为 null 时抛出 400 异常
     */
    @ValidateLog(value = VALIDATE_ENUM_NO_EMPTY, logLevel = ValidateLog.LogLevel.DEBUG)
    public <E> void validateEnumNotNull(E enumValue, String enumName, String businessScene) {
        if (ObjectUtil.isNull(enumValue)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, enumName));
        }
    }

    /**
     * 单条数据库操作结果校验（INSERT / UPDATE / DELETE）。
     * <p>
     * 期望影响行数恰好为 1，否则视为操作失败。
     *
     * @param rows 实际影响行数
     * @throws BizException 影响行数不为 1 时抛出 500 异常
     */
    public void validateDbOperateResult(int rows) {
        if (rows != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
        }
    }

    /**
     * 批量数据库操作结果校验。
     * <p>
     * 允许影响行数少于传入 ID 数（部分 ID 不存在），但不能全量为 0。
     *
     * @param rows          实际影响行数
     * @param expectMinRows 期望最小行数（通常为批量 ID 数量，仅作参考日志用）
     * @throws BizException 影响行数 ≤ 0 时抛出 500 异常
     */
    public void validateBatchDbOperateResult(int rows, int expectMinRows) {
        if (rows <= 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
        }
    }

    /**
     * 批量 ID 存在性校验。
     * <p>
     * 若 ID 列表中有任意 ID 在数据库中不存在，抛出异常并在错误信息中列出缺失的 ID。
     *
     * @param ids         被校验的 ID 列表
     * @param mapper      对应表的 MyBatis-Plus Mapper
     * @param notExistMsg 不存在时的提示（支持 {@code %s} 占位符，传入缺失 ID 列表）
     * @param <T>         实体类型
     * @throws BizException ID 列表为空或存在不存在的 ID 时抛出异常
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
            List<Long> notExistIds = ids.stream()
                    .filter(id -> !existIds.contains(id))
                    .collect(Collectors.toList());
            throw new BizException(HttpStatus.NOT_FOUND, String.format(notExistMsg, notExistIds));
        }
    }

    /**
     * 单 ID 存在性校验。
     * <p>
     * 仅校验数据是否存在，ID 格式合法性已由 {@code @ValidId} 注解提前处理。
     *
     * @param id          被校验的 ID
     * @param mapper      对应表的 MyBatis-Plus Mapper
     * @param notExistMsg 不存在时的提示
     * @param <T>         实体类型
     * @return 查询到的实体对象（不为 null）
     * @throws BizException 记录不存在时抛出 404 异常
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
     * 时间范围校验：开始时间不能晚于结束时间。
     * <p>
     * 任意一方为 null 时跳过校验。
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @throws BizException 开始时间晚于结束时间时抛出 400 异常
     */
    public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new BizException(HttpStatus.BAD_REQUEST, COMMON_TIME_INVALID_ERROR);
        }
    }

    /**
     * 最小值 ≤ 最大值 范围校验（泛型，适配 Integer / Double 等 Comparable 类型）。
     * <p>
     * 任意一方为 null 时跳过校验。
     *
     * @param min    最小值
     * @param max    最大值
     * @param bizKey 字段中文名称（用于错误提示）
     * @param <T>    实现 {@link Comparable} 的数值类型
     * @throws BizException 最小值大于最大值时抛出 400 异常
     */
    public <T extends Comparable<T>> void validateMinMaxRange(T min, T max, String bizKey) {
        if (ObjectUtil.isNotNull(min) && ObjectUtil.isNotNull(max) && min.compareTo(max) > 0) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_MIN_MAX_INVALID_ERROR, bizKey));
        }
    }
}