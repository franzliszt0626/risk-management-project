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
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常（自定义）- 优先级最高
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        // 日志级别为WARN，记录异常消息和栈信息
        log.warn("【业务异常】{}", e.getMessage(), e);
        return Result.error(e.getStatus(), e.getMessage());
    }

    // ======================== 数据库相关异常 ========================

    /**
     * 主键/唯一索引冲突异常（最常见的数据库异常）
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("【数据库异常-唯一约束冲突】{}", e.getMessage(), e);
        return Result.error(HttpStatus.CONFLICT, "数据重复，请检查后重试！");
    }

    /**
     * 空结果集异常（如查询不到数据时的特定异常）
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Result<?> handleEmptyResultException(EmptyResultDataAccessException e) {
        log.warn("【数据库异常-空结果集】{}", e.getMessage(), e);
        return Result.error(HttpStatus.NOT_FOUND, "未查询到相关数据！");
    }

    /**
     * 通用数据库异常（兜底）
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("【数据库异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
    }

    // ======================== 参数校验相关异常 ========================

    /**
     * 请求体参数校验异常（@Valid/@Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder(FailureMessages.COMMON_PARAM_VERIFY_ERROR).append("：");

        // 优化格式：去除多余空格，用分号分隔
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            sb.append(error.getField()).append("：").append(error.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                sb.append("；");
            }
        }
        String errorMsg = sb.toString();
        log.warn("【参数校验异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    /**
     * 请求参数绑定异常（如表单提交、路径参数）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder(FailureMessages.COMMON_BIND_PARAM_ERROR).append("：");

        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            sb.append(error.getField()).append("：").append(error.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                sb.append("；");
            }
        }
        String errorMsg = sb.toString();
        log.warn("【参数绑定异常】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }

    // ======================== JSON解析相关异常 ========================

    /**
     * JSON解析异常（如参数格式错误、枚举值不合法）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleJsonParseError(HttpMessageNotReadableException e) {
        String message = FailureMessages.COMMON_JSON_PARSE_ERROR;
        // 1. 获取最底层的根异常
        Throwable rootCause = getRootCause(e);

        // 2. 优先判断根异常是否是IllegalArgumentException（枚举解析异常）
        if (rootCause instanceof IllegalArgumentException) {
            String causeMsg = rootCause.getMessage();
            if (StrUtil.isNotBlank(causeMsg)) {
                message = causeMsg;
            }
        }
        // 3. 兼容：如果根异常是ValueInstantiationException，再取它的消息
        else if (rootCause instanceof com.fasterxml.jackson.databind.exc.ValueInstantiationException) {
            String causeMsg = rootCause.getMessage();
            if (StrUtil.isNotBlank(causeMsg)) {
                // 提取括号里的具体异常信息（如"预警等级不能为空！"）
                if (causeMsg.contains("problem: ")) {
                    message = causeMsg.split("problem: ")[1].split(";")[0].trim();
                }
            }
        }

        log.warn("【JSON解析异常】{}", message, e);
        return Result.error(HttpStatus.BAD_REQUEST, message);
    }

    // ======================== 常见运行时异常 ========================

    /**
     * 空指针异常（单独处理，避免走到兜底异常）
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("【空指针异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_NULL_POINTER_ERROR);
    }

    /**
     * 处理参数类型不匹配异常（比如路径参数传字符串）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String paramName = e.getName();
        String invalidValue = e.getValue() == null ? "空值" : e.getValue().toString();
        String expectedType = e.getRequiredType() == null ? "数字" : e.getRequiredType().getSimpleName();
        String errorMsg = String.format("参数【%s】格式错误，请传入有效的%s类型值，当前值：%s", paramName, expectedType, invalidValue);

        log.warn("【参数类型不匹配】{}", errorMsg, e);
        return Result.error(HttpStatus.BAD_REQUEST, errorMsg);
    }


    /**
     * 通用运行时异常（如数组越界、类型转换等）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("【运行时异常】{}", e.getMessage(), e);
        String message = String.format(FailureMessages.COMMON_RUNTIME_ERROR, e.getMessage());
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // ======================== 兜底异常（最后处理） ========================

    /**
     * 所有未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleUnexpectedException(Exception e) {
        log.error("【系统未知异常】{}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_SYSTEM_ERROR);
    }

    /**
     * 递归获取最底层的根异常
     */
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null) {
            return throwable;
        }
        return getRootCause(cause);
    }
}