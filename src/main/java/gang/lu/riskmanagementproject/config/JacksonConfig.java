package gang.lu.riskmanagementproject.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import gang.lu.riskmanagementproject.domain.enums.*;
import gang.lu.riskmanagementproject.domain.enums.serializer.ValueEnumDeserializerFactory;
import gang.lu.riskmanagementproject.domain.enums.serializer.ValueEnumSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 14:39
 * @description Jackson 全局时间格式、枚举转换配置
 */
@Configuration
public class JacksonConfig {
    /**
     * 全局时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 1. 时间序列化/反序列化
            builder.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
            builder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));

            // 2. 枚举序列化/反序列化（全局配置）
            SimpleModule enumModule = new SimpleModule();
            // 注册各个枚举的序列化器
            enumModule.addSerializer(AlertLevel.class, new ValueEnumSerializer());
            enumModule.addSerializer(AreaRiskLevel.class, new ValueEnumSerializer());
            enumModule.addSerializer(RiskLevel.class, new ValueEnumSerializer());
            enumModule.addSerializer(Status.class, new ValueEnumSerializer());
            enumModule.addSerializer(WorkType.class, new ValueEnumSerializer());
            // 注册各个枚举的反序列化器
            enumModule.addDeserializer(AlertLevel.class, ValueEnumDeserializerFactory.getDeserializer(AlertLevel.class));
            enumModule.addDeserializer(AreaRiskLevel.class, ValueEnumDeserializerFactory.getDeserializer(AreaRiskLevel.class));
            enumModule.addDeserializer(RiskLevel.class, ValueEnumDeserializerFactory.getDeserializer(RiskLevel.class));
            enumModule.addDeserializer(Status.class, ValueEnumDeserializerFactory.getDeserializer(Status.class));
            enumModule.addDeserializer(WorkType.class, ValueEnumDeserializerFactory.getDeserializer(WorkType.class));
            builder.modules(enumModule);
        };
    }

}
