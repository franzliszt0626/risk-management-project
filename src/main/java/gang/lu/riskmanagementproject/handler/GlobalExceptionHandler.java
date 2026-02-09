package gang.lu.riskmanagementproject.handler;

import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.common.FailureMessages;
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

import java.util.List;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:55
 * @description 全局异常处理器
 * 统一处理系统各类异常，返回标准化Result格式，便于前端统一解析
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    public static final String PROBLEM = "problem: ";

    /**
     * 提取字段校验异常的错误信息（复用MethodArgumentNotValidException和BindException的处理逻辑）
     */
    private String extractFieldErrorMsg(List<FieldError> fieldErrors, String prefix) {
        if (fieldErrors == null || fieldErrors.isEmpty()) {
            return prefix;
        }
        StringBuilder sb = new StringBuilder(prefix).append("：");
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            sb.append(error.getField()).append("：").append(error.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                sb.append("；");
            }
        }
        return sb.toString();
    }

    /**
     * 递归获取最底层的根异常（解耦，便于复用和测试）
     */
    private Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable cause = throwable.getCause();
        return cause == null ? throwable : getRootCause(cause);
    }

    // ======================== 自定义业务异常（优先级最高） ========================

    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        log.warn("【业务异常】{}", e.getMessage(), e);
        return Result.error(e.getStatus(), e.getMessage());
    }

    // ======================== 数据库相关异常 ========================

    /**
     * 主键/唯一索引冲突异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("【数据库唯一约束冲突】{}", e.getMessage(), e);
        return Result.error(HttpStatus.CONFLICT, "数据重复，请检查后重试！");
    }

    /**
     * 空结果集异常
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Result<?> handleEmptyResultException(EmptyResultDataAccessException e) {
        log.warn("【数据库空结果集】{}", e.getMessage(), e);
        return Result.error(HttpStatus.NOT_FOUND, "未查询到相关数据！");
    }

    /**
     * 通用数据库异常（兜底）
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("【数据库通用异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
    }

    // ======================== 参数校验/绑定相关异常 ========================

    /**
     * 请求体参数校验异常（@Valid/@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = extractFieldErrorMsg(e.getBindingResult().getFieldErrors(), FailureMessages.COMMON_PARAM_VERIFY_ERROR);
        log.warn("【请求体参数校验异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 请求参数绑定异常（表单/路径参数）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String errorMsg = extractFieldErrorMsg(e.getBindingResult().getFieldErrors(), FailureMessages.COMMON_BIND_PARAM_ERROR);
        log.warn("【参数绑定异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 参数类型不匹配异常（路径/请求参数类型错误）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String invalidValue = e.getValue() == null ? "空值" : e.getValue().toString();
        String expectedType = e.getRequiredType() == null ? "数字" : e.getRequiredType().getSimpleName();
        String errorMsg = String.format("参数【%s】格式错误，请传入有效的%s类型值（当前值：%s）", paramName, expectedType, invalidValue);

        log.warn("【参数类型不匹配】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    // ======================== JSON解析相关异常 ========================

    /**
     * JSON解析异常（参数格式错误、枚举值不合法等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleJsonParseError(HttpMessageNotReadableException e) {
        String message = FailureMessages.COMMON_JSON_PARSE_ERROR;
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

    // ======================== 通用运行时异常 ========================

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("【空指针异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_NULL_POINTER_ERROR);
    }

    /**
     * 通用运行时异常（数组越界、类型转换等）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("【运行时异常】{}", e.getMessage(), e);
        String message = String.format(FailureMessages.COMMON_RUNTIME_ERROR, e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // ======================== 兜底异常（所有未捕获的异常） ========================

    @ExceptionHandler(Exception.class)
    public Result<?> handleUnexpectedException(Exception e) {
        log.error("【系统未知异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_SYSTEM_ERROR);
    }
}