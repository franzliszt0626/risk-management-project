package gang.lu.riskmanagementproject.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:54
 * @description 全局业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public BizException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public BizException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }
}