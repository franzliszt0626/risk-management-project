package gang.lu.riskmanagementproject.annotation;

import java.lang.annotation.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 13:14
 * @description 自定义注解，记录业务操作日志
 */
@Target({ElementType.METHOD}) // 仅作用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
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
}