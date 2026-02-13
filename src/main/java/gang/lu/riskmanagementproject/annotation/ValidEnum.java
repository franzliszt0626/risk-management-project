package gang.lu.riskmanagementproject.annotation;

import gang.lu.riskmanagementproject.validator.EnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:39
 * @description 枚举校验
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {

    /**
     * 枚举类型
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 业务标识（用于提示）
     */
    String bizName() default "枚举值";

    boolean allowBlank() default true;

    String message() default "【参数校验失败】无效的{bizName}（当前值：{currentValue}）！允许值为：{enumValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}