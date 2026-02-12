package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.annotation.ValidEnum;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.BusinessConstants.VALUE;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:40
 * @description 枚举校验器
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private String bizName;
    private List<String> enumValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        this.bizName = constraintAnnotation.bizName();
        this.enumValues = EnumUtil.getFieldValues(enumClass, VALUE).stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return true;
        }
        String trimValue = value.trim();
        boolean isValid = enumValues.contains(trimValue);
        if (!isValid) {
            // 替换占位符，返回友好提示
            String message = context.getDefaultConstraintMessageTemplate()
                    .replace("{bizName}", bizName)
                    .replace("{enumValues}", StringUtils.join(enumValues, "、"));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }
}
