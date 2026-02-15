package gang.lu.riskmanagementproject.validator.annotation;

import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/11 23:40
 * @description 枚举校验器
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private String bizName;
    private List<String> enumValues;
    private boolean allowBlank;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        this.bizName = constraintAnnotation.bizName();
        this.allowBlank = constraintAnnotation.allowBlank();
// 通用获取ValueEnum的value值，替代硬编码的EnumUtil
        this.enumValues = Stream.of(enumClass.getEnumConstants())
                .map(enumConstant -> ((ValueEnum<?>) enumConstant).getValue().toString())
                .collect(Collectors.toList());
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return allowBlank;
        }
        String trimValue = value.trim();
        boolean isValid = enumValues.contains(trimValue);
        if (!isValid) {
            String message = context.getDefaultConstraintMessageTemplate()
                    .replace("{bizName}", bizName)
                    .replace("{enumValues}", StringUtils.join(enumValues, "、"))
                    .replace("{currentValue}", trimValue);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }
}
