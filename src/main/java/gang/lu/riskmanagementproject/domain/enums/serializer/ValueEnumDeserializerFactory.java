package gang.lu.riskmanagementproject.domain.enums.serializer;

import com.fasterxml.jackson.databind.JsonDeserializer;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 16:14
 * @description 枚举反序列化器工厂（用于Jackson全局配置）
 */
public class ValueEnumDeserializerFactory {
    /**
     * 获取指定枚举类的反序列化器
     */
    public static <T extends Enum<T> & ValueEnum<String>> JsonDeserializer<T> getDeserializer(Class<T> enumClass) {
        return new ValueEnumDeserializer<>(enumClass);
    }
}
