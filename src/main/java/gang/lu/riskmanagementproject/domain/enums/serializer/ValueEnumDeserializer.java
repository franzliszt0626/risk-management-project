package gang.lu.riskmanagementproject.domain.enums.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link ValueEnum} 枚举反序列化器。
 * <p>
 * 将前端传入的中文字符串（如 "低风险"）反查为对应的枚举常量，
 * 匹配失败时抛出 {@link IllegalArgumentException}，错误信息中列出所有合法值，
 * 由全局异常处理器统一捕获并返回 400 响应。
 * <p>
 * 匹配规则：去除首尾空格后与 {@link ValueEnum#getValue()} 做精确等值比较。
 *
 * @author Franz Liszt
 * @since 2026-02-12
 */
public class ValueEnumDeserializer extends JsonDeserializer<ValueEnum<?>> {

    /**
     * 反序列化：将 JSON 字符串还原为对应枚举常量。
     *
     * @param p    JSON 解析器
     * @param ctxt 反序列化上下文（用于获取目标类型）
     * @return 匹配的枚举常量，输入为空时返回 null
     * @throws IOException              JSON 读取异常
     * @throws IllegalArgumentException 输入值不在枚举合法值列表中
     */
    @Override
    public ValueEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String inputValue = p.getText().trim();
        if (StrUtil.isBlank(inputValue)) {
            return null;
        }

        // 获取目标枚举类型并校验合法性
        JavaType targetType = ctxt.getContextualType();
        Class<?> rawClass = targetType.getRawClass();
        if (!rawClass.isEnum() || !ValueEnum.class.isAssignableFrom(rawClass)) {
            throw new IllegalArgumentException(
                    "目标类型 [" + rawClass.getName() + "] 不是实现 ValueEnum 的枚举类");
        }

        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) rawClass;

        // 遍历枚举常量，精确匹配 value
        for (Enum<?> constant : enumClass.getEnumConstants()) {
            ValueEnum<?> valueEnum = (ValueEnum<?>) constant;
            if (Objects.equals(valueEnum.getValue().toString(), inputValue)) {
                return valueEnum;
            }
        }

        // 匹配失败：拼接合法值列表，抛出带提示信息的异常
        String allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(c -> ((ValueEnum<?>) c).getValue().toString())
                .collect(Collectors.joining("、"));
        throw new IllegalArgumentException(
                enumClass.getSimpleName() + " 值不合法（当前值：" + inputValue + "），允许值：" + allowedValues);
    }

    /**
     * 声明此反序列化器处理的顶层类型，使 Jackson 能正确注册。
     *
     * @return {@link ValueEnum} 接口类型
     */
    @Override
    public Class<?> handledType() {
        return ValueEnum.class;
    }
}