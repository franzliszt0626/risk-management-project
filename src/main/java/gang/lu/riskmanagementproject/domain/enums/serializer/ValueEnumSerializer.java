package gang.lu.riskmanagementproject.domain.enums.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

import java.io.IOException;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 16:13
 * @description ValueEnum枚举序列化器（将枚举对象序列化为value字符串）
 */
public class ValueEnumSerializer extends JsonSerializer<ValueEnum<?>> {
    @Override
    public void serialize(ValueEnum<?> valueEnum, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (valueEnum == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(valueEnum.getValue().toString());
    }
}