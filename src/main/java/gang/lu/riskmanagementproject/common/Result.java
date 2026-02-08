package gang.lu.riskmanagementproject.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:45
 * @description 统一返回结果
 */
@Data
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty(value = "业务状态码（1=成功，0=失败）", example = "1")
    private Integer code;

    @ApiModelProperty(value = "响应消息", example = "操作成功")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "HTTP 状态码", example = "200")
    private Integer httpStatus;


    private Result(Integer code, String message, T data, Integer httpStatus) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.httpStatus = httpStatus;
    }

    /**
     * 成功，无数据
     */
    public static <T> Result<T> ok() {
        return new Result<>(1, "success", null, HttpStatus.OK.value());
    }


    /**
     * 成功，带数据
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(1, "success", data, HttpStatus.OK.value());
    }

    /**
     * 成功，自定义消息+数据
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(1, message, data, HttpStatus.OK.value());
    }

    /**
     * 成功，仅自定义消息
     */
    public static <T> Result<T> ok(String message) {
        return new Result<>(1, message, null, HttpStatus.OK.value());
    }


    /**
     * 失败，通用错误
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(0, message, null, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 带HTTP的错误
     */
    public static <T> Result<T> error(HttpStatus status, String message) {
        return new Result<>(0, message, null, status.value());
    }

    /**
     * 自定义构建
     */
    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder<>();
    }

    /**
     * 自定义构建
     */
    public static class ResultBuilder<T> {
        private Integer code = 1;
        private String message = "success";
        private T data;
        private Integer httpStatus = HttpStatus.OK.value();

        public ResultBuilder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public ResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResultBuilder<T> httpStatus(Integer httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Result<T> build() {
            return new Result<>(code, message, data, httpStatus);
        }
    }
}