package gang.lu.riskmanagementproject.validator.annotation;

import gang.lu.riskmanagementproject.annotation.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static gang.lu.riskmanagementproject.common.FailedMessages.*;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:37
 * @description id校验器
 */
public class IdValidator implements ConstraintValidator<ValidId, Long> {
    private String bizName;

    @Override
    public void initialize(ValidId constraintAnnotation) {
        this.bizName = constraintAnnotation.bizName();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        // 1. 空值校验
        if (Objects.isNull(id)) {
            setCustomMessage(context, String.format(COMMON_PARAM_EMPTY_ERROR_WITHOUT_PREFIX, bizName));
            return false;
        }
        // 2. 正整数校验
        if (id <= 0) {
            setCustomMessage(context, String.format(COMMON_INVALID_ID_ERROR_WITHOUT_PREFIX, bizName));
            return false;
        }
        return true;
    }

    /**
     * 设置自定义校验提示信息
     */
    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
