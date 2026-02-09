package gang.lu.riskmanagementproject.annotation;

import java.lang.annotation.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 14:05
 * @description 自定义注解：标记需要记录校验日志的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateLog {
    /**
     * 校验场景名称（如"ID合法性校验"、"工号唯一性校验"）
     */
    String value();

    /**
     * 是否记录入参（默认true）
     */
    boolean recordParams() default true;
}