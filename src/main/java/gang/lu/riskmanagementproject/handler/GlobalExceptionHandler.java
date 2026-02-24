package gang.lu.riskmanagementproject.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 全局异常处理器
 * <p>
 * 统一捕获并处理系统所有异常，返回标准化 {@link Result} 格式响应，便于前端统一解析。
 *
 * <p><b>处理优先级（由高到低）：</b>
 * <ol>
 *   <li>自定义业务异常 {@link BizException}</li>
 *   <li>参数校验 / 绑定异常</li>
 *   <li>JSON 解析异常</li>
 *   <li>数据库相关异常</li>
 *   <li>通用运行时异常</li>
 *   <li>兜底异常</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String JACKSON_PROBLEM_DELIMITER = "problem: ";

    // ========================1. 自定义业务异常（最高优先级）==========================

    /**
     * 处理自定义业务异常 {@link BizException}。
     * <p>
     * 4xx 客户端错误使用 WARN 级别；5xx 服务端错误使用 ERROR 级别，
     * 避免将客户端传参错误误报为服务端故障。
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        if (e.getStatus().is5xxServerError()) {
            log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        } else {
            log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, e.getMessage(), e);
        }
        return Result.error(e.getStatus(), e.getMessage());
    }

    /**
     * 处理非法参数异常（如枚举转换失败、参数值不合法等）。
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, e.getMessage(), e);
        // 若异常信息本身已包含枚举合法值提示，直接透传；否则使用通用模板
        String msg = (e.getMessage() != null
                && e.getMessage().contains(INVALID)
                && e.getMessage().contains(ALLOW))
                ? e.getMessage()
                : String.format(COMMON_PARAM_ILLEGAL_ERROR, e.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    // ==============================2. 参数校验 / 绑定异常===========================

    /**
     * 处理路径参数 / 请求参数上 {@code @Validated} 注解触发的校验异常。
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String msg = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("；"));
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, msg, e);
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * 处理请求体 {@code @Valid / @Validated} 注解触发的校验异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = extractFirstFieldError(e.getBindingResult().getFieldErrors());
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, msg, e);
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * 处理表单 / 路径参数绑定异常。
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = extractFirstFieldError(e.getBindingResult().getFieldErrors());
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, msg, e);
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    /**
     * 处理路径参数 / 请求参数类型不匹配异常（如期望 Long 却传入字符串）。
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String invalidValue = e.getValue() == null ? EMPTY : e.getValue().toString();
        String expectedType = e.getRequiredType() == null ? NUMBER : e.getRequiredType().getSimpleName();
        String msg = String.format(COMMON_PARAM_TYPE_MISMATCH_ERROR, paramName, expectedType, invalidValue);
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, msg, e);
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    // =============================3. JSON 解析异常=============================

    /**
     * 处理 JSON 解析异常（参数格式错误、枚举值不合法等）。
     * <p>
     * 优先提取 Jackson {@code ValueInstantiationException} 中的 "problem:" 后的具体描述；
     * 其次使用根因的 message；最后降级为通用提示。
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleJsonParseException(HttpMessageNotReadableException e) {
        String msg = COMMON_JSON_PARSE_ERROR;
        Throwable root = getRootCause(e);

        if (root instanceof com.fasterxml.jackson.databind.exc.ValueInstantiationException) {
            String causeMsg = root.getMessage();
            if (StrUtil.isNotBlank(causeMsg) && causeMsg.contains(JACKSON_PROBLEM_DELIMITER)) {
                msg = causeMsg.split(JACKSON_PROBLEM_DELIMITER)[1].split(";")[0].trim();
            }
        } else if (root instanceof IllegalArgumentException && StrUtil.isNotBlank(root.getMessage())) {
            msg = root.getMessage();
        }

        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, msg, e);
        return Result.error(HttpStatus.BAD_REQUEST, msg);
    }

    // ==============================4. 数据库相关异常================================

    /**
     * 处理主键 / 唯一索引冲突异常。
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        return Result.error(HttpStatus.CONFLICT, COMMON_DUPLICATE_KEY_ERROR);
    }

    /**
     * 处理空结果集异常。
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Result<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.warn("{}{}", COMMON_BIZ_EXCEPTION_CLIENT, e.getMessage(), e);
        return Result.error(HttpStatus.NOT_FOUND, COMMON_EMPTY_RESULT_ERROR);
    }

    /**
     * 处理通用数据库异常（兜底，优先级低于以上两个数据库异常）。
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
    }

    // ==================================5. 通用运行时异常================================

    /**
     * 处理空指针异常。
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_NULL_POINTER_ERROR);
    }

    /**
     * 处理通用运行时异常（数组越界、类型转换等）。
     * <p>
     * <b>注意：</b>{@link BizException}、{@link IllegalArgumentException} 均是
     * {@link RuntimeException} 的子类，但 Spring 会优先匹配更具体的处理器，
     * 此处只会兜底处理上方未覆盖的运行时异常。
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format(COMMON_RUNTIME_ERROR, e.getMessage()));
    }

    // =================6. 兜底异常（所有未捕获的 Checked Exception）=====================

    /**
     * 兜底处理所有未被前面覆盖的异常（最低优先级）。
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleUnexpectedException(Exception e) {
        log.error("{}{}", COMMON_BIZ_EXCEPTION_SERVER, e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_SYSTEM_ERROR);
    }

    // =================================私有工具方法====================================

    /**
     * 提取字段校验异常列表中第一条错误的 defaultMessage。
     * <p>
     * 只取第一条，避免前端看到一长串错误文案。
     *
     * @param fieldErrors 字段错误列表
     * @return 错误提示信息
     */
    private String extractFirstFieldError(List<FieldError> fieldErrors) {
        if (ObjectUtil.isNull(fieldErrors) || fieldErrors.isEmpty()) {
            return COMMON_PARAM_VERIFY_ERROR;
        }
        return fieldErrors.get(0).getDefaultMessage();
    }

    /**
     * 递归获取异常链最底层的根因。
     *
     * @param throwable 原始异常
     * @return 根因异常
     */
    private Throwable getRootCause(Throwable throwable) {
        if (ObjectUtil.isNull(throwable)) {
            return null;
        }
        Throwable cause = throwable.getCause();
        return ObjectUtil.isNull(cause) ? throwable : getRootCause(cause);
    }
}