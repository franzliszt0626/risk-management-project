package gang.lu.riskmanagementproject.converter;


import gang.lu.riskmanagementproject.domain.enums.WorkType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 17:01
 * @description 工种转换器
 */

@Component
public class WorkTypeConverter implements Converter<String, WorkType> {

    @Override
    public WorkType convert(@NonNull String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        return WorkType.fromValue(source.trim());
    }
}