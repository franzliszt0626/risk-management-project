package gang.lu.riskmanagementproject.converter;




import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 16:47
 * @description 预警级别枚举转换器
 */
@Component
public class AlertLevelConverter implements Converter<String, AlertLevel> {

    @Override
    public AlertLevel convert(@NonNull String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        // 调用枚举的fromValue方法，根据自定义value解析
        return AlertLevel.fromValue(source.trim());
    }
}