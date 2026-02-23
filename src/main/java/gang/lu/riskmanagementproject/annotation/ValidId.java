package gang.lu.riskmanagementproject.annotation;


import gang.lu.riskmanagementproject.validator.annotation.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static gang.lu.riskmanagementproject.common.field.FieldEnglishConstants.UPCAST_ID;
import static gang.lu.riskmanagementproject.message.FailedMessages.COMMON_ID_VALIDATE_ERROR;

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


    String bizName() default UPCAST_ID;

    String message() default COMMON_ID_VALIDATE_ERROR;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}