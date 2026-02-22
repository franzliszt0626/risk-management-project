package gang.lu.riskmanagementproject.domain.enums.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

import java.io.IOException;

/**
 * {@link ValueEnum} 枚举序列化器。
 * <p>
 * 将枚举实例序列化为其 {@link ValueEnum#getValue()} 对应的字符串，
 * 而非默认的枚举名称（如 "LOW_RISK"），确保前端接收到中文业务值（如 "低风险"）。
 * <p>
 * 由于各枚举字段已标注 {@code @JsonValue}，通常情况下 Jackson 会自动处理序列化，
 * 此序列化器作为全局兜底配置注册到 {@code ObjectMapper}。
 *
 * @author Franz Liszt
 * @since 2026-02-12
 */
public class ValueEnumSerializer extends JsonSerializer<ValueEnum<?>> {

    @Override
    public void serialize(ValueEnum<?> valueEnum, JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {
        if (valueEnum == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(valueEnum.getValue().toString());
    }
}
