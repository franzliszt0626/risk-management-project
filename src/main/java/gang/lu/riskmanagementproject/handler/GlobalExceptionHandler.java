package gang.lu.riskmanagementproject.handler;

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

import static gang.lu.riskmanagementproject.common.FailedMessages.*;

/**
 * <p>
 * 全局异常处理器
 * </p>
 * 统一捕获并处理系统所有异常，返回标准化的Result格式响应，便于前端统一解析
 * 异常处理优先级：自定义业务异常 > 框架校验异常 > 数据库异常 > 通用运行时异常 > 兜底异常
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ======================== 常量定义 ========================

    private static final String PROBLEM = "problem: ";
    private static final String PARAM_TYPE_ERROR_TEMPLATE = "参数【%s】格式错误，请传入有效的%s类型值（当前值：%s）";

    // ======================== 私有工具方法 ========================

    /**
     * 提取字段校验异常的错误信息（仅保留自定义提示，去除冗余前缀）
     *
     * @param fieldErrors 字段错误列表
     * @return 拼接后的错误信息
     */
    private String extractFieldErrorMsg(List<FieldError> fieldErrors) {
        if (fieldErrors == null || fieldErrors.isEmpty()) {
            return COMMON_PARAM_VERIFY_ERROR;
        }
        // 只取第一个错误，不再拼接所有错误
        FieldError firstError = fieldErrors.get(0);
        // 拼接统一前缀 + 第一个错误的具体信息
        return firstError.getDefaultMessage();
    }

    /**
     * 递归获取最底层的根异常（解耦，便于复用和测试）
     *
     * @param throwable 原始异常
     * @return 根异常
     */
    private Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = throwable.getCause();
        return cause == null ? throwable : getRootCause(cause);
    }

    // ======================== 1. 自定义业务异常（最高优先级） ========================

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        log.warn("【业务异常】{}", e.getMessage(), e);
        return Result.error(e.getStatus(), e.getMessage());
    }

    /**
     * 处理非法参数异常（如枚举转换失败、参数值不合法等）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("【参数不合法】{}", e.getMessage(), e);
        String errorMsg = e.getMessage().contains("不合法") && e.getMessage().contains("允许值")
                ? e.getMessage()
                : "参数不合法：" + e.getMessage();
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    // ======================== 2. 参数校验/绑定异常 ========================

    /**
     * 处理@Validated（方法参数）的校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMsg = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("；"));
        log.warn("【Controller层方法参数校验异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理请求体参数校验异常（@Valid/@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = extractFieldErrorMsg(e.getBindingResult().getFieldErrors());
        log.warn("【请求体参数校验异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理请求参数绑定异常（表单/路径参数）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String errorMsg = extractFieldErrorMsg(e.getBindingResult().getFieldErrors());
        log.warn("【参数绑定异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理参数类型不匹配异常（路径/请求参数类型错误）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String invalidValue = e.getValue() == null ? "空值" : e.getValue().toString();
        String expectedType = e.getRequiredType() == null ? "数字" : e.getRequiredType().getSimpleName();
        String errorMsg = String.format(PARAM_TYPE_ERROR_TEMPLATE, paramName, expectedType, invalidValue);

        log.warn("【参数类型不匹配】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    // ======================== 3. JSON解析异常 ========================

    /**
     * 处理JSON解析异常（参数格式错误、枚举值不合法等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleJsonParseError(HttpMessageNotReadableException e) {
        String message = COMMON_JSON_PARSE_ERROR;
        Throwable rootCause = getRootCause(e);

        // 枚举解析异常
        if (rootCause instanceof IllegalArgumentException && StrUtil.isNotBlank(rootCause.getMessage())) {
            message = rootCause.getMessage();
        }
        // Jackson实例化异常（提取具体错误信息）
        else if (rootCause instanceof com.fasterxml.jackson.databind.exc.ValueInstantiationException) {
            String causeMsg = rootCause.getMessage();
            if (StrUtil.isNotBlank(causeMsg) && causeMsg.contains(PROBLEM)) {
                message = causeMsg.split(PROBLEM)[1].split(";")[0].trim();
            }
        }

        log.warn("【JSON解析异常】{}", message, e);
        return Result.error(HttpStatus.BAD_REQUEST, message);
    }

    // ======================== 4. 数据库相关异常 ========================

    /**
     * 处理主键/唯一索引冲突异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("【数据库唯一约束冲突】{}", e.getMessage(), e);
        return Result.error(HttpStatus.CONFLICT, "数据重复，请检查后重试！");
    }

    /**
     * 处理空结果集异常
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Result<?> handleEmptyResultException(EmptyResultDataAccessException e) {
        log.warn("【数据库空结果集】{}", e.getMessage(), e);
        return Result.error(HttpStatus.NOT_FOUND, "未查询到相关数据！");
    }

    /**
     * 处理通用数据库异常（兜底）
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("【数据库通用异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_DATABASE_ERROR);
    }

    // ======================== 5. 通用运行时异常 ========================

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("【空指针异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_NULL_POINTER_ERROR);
    }

    /**
     * 处理通用运行时异常（数组越界、类型转换等）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("【运行时异常】{}", e.getMessage(), e);
        String message = String.format(COMMON_RUNTIME_ERROR, e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // ======================== 6. 兜底异常（所有未捕获的异常） ========================

    /**
     * 处理所有未捕获的异常（最低优先级）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleUnexpectedException(Exception e) {
        log.error("【系统未知异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, COMMON_SYSTEM_ERROR);
    }
}