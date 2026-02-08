package gang.lu.riskmanagementproject.converter;



import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 16:37
 * @description 区域风险枚举转换类
 */
@Component
public class AreaRiskLevelConverter implements Converter<String, AreaRiskLevel> {

    @Override
    public AreaRiskLevel convert(@NonNull String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        // 调用枚举的fromValue方法，根据自定义value解析
        return AreaRiskLevel.fromValue(source.trim());
    }
}