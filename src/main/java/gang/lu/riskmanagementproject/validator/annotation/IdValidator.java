package gang.lu.riskmanagementproject.validator.annotation;

import gang.lu.riskmanagementproject.annotation.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * {@link ValidId} 注解对应的约束校验器。
 * <p>
 * 对 {@code Long} 类型的 ID 做两层校验：
 * <ol>
 *   <li>非空：ID 不能为 null；</li>
 *   <li>正整数：ID 必须大于 0（数据库自增主键不允许 0 或负数）。</li>
 * </ol>
 * 校验不通过时使用 {@link ConstraintValidatorContext} 回写自定义错误信息，
 * 替换默认的 Bean Validation 模板提示。
 *
 * @author Franz Liszt
 * @since 2026-02-11
 */
public class IdValidator implements ConstraintValidator<ValidId, Long> {

    /**
     * ID 对应的业务名称，如"工人ID"、"预警记录ID"
     */
    private String bizName;

    /**
     * 初始化：读取 {@link ValidId#bizName()} 属性。
     *
     * @param annotation {@link ValidId} 注解实例
     */
    @Override
    public void initialize(ValidId annotation) {
        this.bizName = annotation.bizName();
    }

    /**
     * 校验逻辑：
     * <ol>
     *   <li>null → 提示"不能为空"；</li>
     *   <li>≤ 0  → 提示"无效 ID"。</li>
     * </ol>
     *
     * @param id      请求中传入的 ID 值
     * @param context 约束校验上下文
     * @return 校验是否通过
     */
    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (Objects.isNull(id)) {
            reject(context, String.format(COMMON_PARAM_EMPTY_ERROR_WITHOUT_PREFIX, bizName));
            return false;
        }
        if (id <= 0) {
            reject(context, String.format(COMMON_INVALID_ID_ERROR_WITHOUT_PREFIX, bizName));
            return false;
        }
        return true;
    }

    /**
     * 禁用默认模板并写入自定义错误提示。
     *
     * @param context 约束校验上下文
     * @param message 自定义错误信息
     */
    private void reject(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}