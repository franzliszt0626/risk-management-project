package gang.lu.riskmanagementproject.domain.enums.serializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

import java.io.IOException;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 16:13
 * @description ValueEnum枚举反序列化器
 */
public class ValueEnumDeserializer<T extends Enum<T> & ValueEnum<String>> extends JsonDeserializer<T> {

    private final Class<T> enumClass;

    public ValueEnumDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return ValueEnum.fromValue(enumClass, value);
    }
}