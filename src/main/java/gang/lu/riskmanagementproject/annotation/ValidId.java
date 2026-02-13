package gang.lu.riskmanagementproject.annotation;


import gang.lu.riskmanagementproject.validator.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:36
 * @description 校验id（非空+正整数）
 */
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdValidator.class)
public @interface ValidId {


    String bizName() default "ID";

    String message() default "【参数校验失败】无效的{bizName}，请传入正整数！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}