package gang.lu.riskmanagementproject.annotation;

import java.lang.annotation.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 13:14
 * @description 自定义注解，记录业务操作日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessLog {
    /**
     * 业务操作名称（如"新增预警记录"、"删除预警记录"）
     */
    String value();

    /**
     * 是否记录入参（默认true）
     */
    boolean recordParams() default true;

    /**
     * 是否记录返回值（默认false，敏感场景可关闭）
     */
    boolean recordResult() default false;

    /**
     * 是否记录请求上下文（IP、请求方式、路径等，默认true）
     */
    boolean recordRequestContext() default true;

    /**
     * 日志级别（默认INFO）
     */
    LogLevel logLevel() default LogLevel.INFO;

    /**
     * 日志级别枚举
     */
    enum LogLevel {
        /**
         * 日志级别枚举
         */
        DEBUG, INFO, WARN, ERROR
    }
}