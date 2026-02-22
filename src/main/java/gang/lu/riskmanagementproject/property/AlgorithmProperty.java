package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:05
 * @description
 */
@Data
@Component
public class AlgorithmProperty {

    @Value("${algorithm.base-url}")
    private String BASE_URL;

    @Value("${algorithm.connect-timeout:10}")
    private int CONNECT_TIMEOUT;

    @Value("${algorithm.read-timeout:60}")
    private int READ_TIMEOUT;

    @Value("${algorithm.write-timeout:30}")
    private int WRITE_TIMEOUT;
}
