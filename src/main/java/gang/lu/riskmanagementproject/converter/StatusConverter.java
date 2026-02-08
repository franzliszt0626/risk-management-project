package gang.lu.riskmanagementproject.converter;



import gang.lu.riskmanagementproject.domain.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 17:00
 * @description 工人状态转换器
 */

@Component
public class StatusConverter implements Converter<String, Status> {

    @Override
    public Status convert(@NonNull String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        return Status.fromValue(source.trim());
    }
}