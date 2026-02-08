package gang.lu.riskmanagementproject.converter;


import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 16:59
 * @description RiskLevel 枚举转换器：将前端传入的"低风险/中风险/高风险/严重风险"转换为对应的枚举
 */
@Component
public class RiskLevelConverter implements Converter<String, RiskLevel> {

    @Override
    public RiskLevel convert(@NonNull String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        return RiskLevel.fromValue(source.trim());
    }
}