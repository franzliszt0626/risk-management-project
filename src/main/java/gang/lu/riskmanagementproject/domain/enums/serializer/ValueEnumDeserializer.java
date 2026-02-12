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
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 16:13
 * @description ValueEnum枚举反序列化器
 */
public class ValueEnumDeserializer extends JsonDeserializer<ValueEnum<?>> {

    @Override
    public ValueEnum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String inputValue = p.getText().trim();
        if (StrUtil.isBlank(inputValue)) {
            return null;
        }

        // 1. 安全获取目标枚举类型（核心：消除未检查转换警告）
        JavaType targetType = ctxt.getContextualType();
        Class<?> rawClass = targetType.getRawClass();

        // 2. 校验是否为枚举且实现 ValueEnum
        if (!rawClass.isEnum() || !ValueEnum.class.isAssignableFrom(rawClass)) {
            throw new IllegalArgumentException("目标类型[" + rawClass.getName() + "]不是实现ValueEnum的枚举类");
        }

        // 3. 安全转换为枚举类
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) rawClass;
        ValueEnum<?> result = null;

        // 4. 遍历枚举常量匹配值
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            ValueEnum<?> valueEnum = (ValueEnum<?>) enumConstant;
            if (Objects.equals(valueEnum.getValue().toString(), inputValue)) {
                result = valueEnum;
                break;
            }
        }

        // 5. 匹配失败则抛异常
        if (result == null) {
            String allowValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(enumConstant -> ((ValueEnum<?>) enumConstant).getValue().toString())
                    .collect(Collectors.joining("、"));
            throw new IllegalArgumentException(
                    enumClass.getSimpleName() + " 值不合法（当前值：" + inputValue + "），允许值：" + allowValues
            );
        }

        return result;
    }

    /**
     * 关键：告诉Jackson这个反序列化器支持的类型（消除上下文类型获取问题）
     */
    @Override
    public Class<?> handledType() {
        return ValueEnum.class;
    }
}