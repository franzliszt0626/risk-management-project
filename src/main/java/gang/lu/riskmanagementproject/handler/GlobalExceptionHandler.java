package gang.lu.riskmanagementproject.handler;

import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static gang.lu.riskmanagementproject.common.FailureMessages.REQUEST_PARAMETER_ERROR_MESSAGE;

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
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        log.warn("BizException: {}", e.getMessage(), e);
        return Result.error(e.getStatus(), e.getMessage());
    }

    /**
     * 数据库异常（如唯一约束、连接失败等）
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("Database error: {}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.DATABASE_ERROR_MESSAGE);
    }

    /**
     * 参数校验异常（JSR-303）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder(FailureMessages.PARAMETER_VERIFY_ERROR_MESSAGE);
        for (FieldError error : fieldErrors) {
            sb.append("[").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("] ");
        }
        log.warn("Validation failed: {}", sb);
        return Result.error(HttpStatus.BAD_REQUEST, sb.toString());
    }

    /**
     * BindException表单绑定错误
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder(REQUEST_PARAMETER_ERROR_MESSAGE);
        for (FieldError error : fieldErrors) {
            sb.append("[").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("] ");
        }
        return Result.error(HttpStatus.BAD_REQUEST, sb.toString());
    }

    /**
     * 其他未预期异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleUnexpectedException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.UNIVERSAL_ERROR_MESSAGE);
    }

    /**
     * 捕获HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleJsonParseError(HttpMessageNotReadableException e) {
        String message = REQUEST_PARAMETER_ERROR_MESSAGE;
        Throwable cause = e.getCause();
        if (cause instanceof IllegalArgumentException) {
            message = cause.getMessage();
        }
        log.warn("JSON 解析失败: {}", message, e);
        return Result.error(HttpStatus.BAD_REQUEST, message);
    }
}