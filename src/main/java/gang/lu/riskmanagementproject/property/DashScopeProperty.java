package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:21
 * @description
 */
@Data
@Component
public class DashScopeProperty {

    @Value("${dashscope.api-key}")
    private String apiKey;

    @Value("${dashscope.model}")
    private String model;

    @Value("${dashscope.max-tokens}")
    private int maxTokens;

    @Value("${dashscope.base-url}")
    private String baseUrl;
}