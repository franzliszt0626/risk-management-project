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
 * {@link ValidEnum} 注解对应的约束校验器
 * <p>
 * 在初始化阶段通过反射读取目标枚举类的所有合法值（基于 {@link ValueEnum#getValue()}），
 * 在校验阶段判断传入的字符串是否属于合法枚举值集合，并将格式化后的错误信息回写到
 * {@link ConstraintValidatorContext}。
 *
 * @author Franz Liszt
 * @since 2026-02-11
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private String bizName;
    private boolean allowBlank;
    /**
     * 枚举所有合法值（字符串形式，初始化时一次性解析）
     */
    private List<String> enumValues;

    /**
     * 初始化：读取注解属性并预加载枚举合法值列表
     *
     * @param annotation {@link ValidEnum} 注解实例
     */
    @Override
    public void initialize(ValidEnum annotation) {
        this.bizName = annotation.bizName();
        this.allowBlank = annotation.allowBlank();
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        this.enumValues = Stream.of(enumClass.getEnumConstants())
                .map(e -> ((ValueEnum<?>) e).getValue().toString())
                .collect(Collectors.toList());
    }

    /**
     * 校验逻辑：
     * <ol>
     *   <li>若值为空且 {@code allowBlank = true}，直接通过；</li>
     *   <li>若值为空且 {@code allowBlank = false}，拦截；</li>
     *   <li>去除首尾空格后与合法值列表比较，不在列表内则构建自定义错误信息。</li>
     * </ol>
     *
     * @param value   请求中传入的字符串值
     * @param context 约束校验上下文
     * @return 校验是否通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return allowBlank;
        }
        String trimmed = value.trim();
        if (enumValues.contains(trimmed)) {
            return true;
        }
        // 构建包含 bizName、合法值列表、当前错误值的自定义提示
        String message = context.getDefaultConstraintMessageTemplate()
                .replace("{bizName}", bizName)
                .replace("{enumValues}", StringUtils.join(enumValues, "、"))
                .replace("{currentValue}", trimmed);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}