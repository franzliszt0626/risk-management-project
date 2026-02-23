package gang.lu.riskmanagementproject.config;

import gang.lu.riskmanagementproject.property.AlgorithmProperty;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:09
 */
@Configuration
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient(AlgorithmProperty props) {
        return new OkHttpClient.Builder()
                .connectTimeout(props.getCONNECT_TIMEOUT(), TimeUnit.SECONDS)
                .readTimeout(props.getREAD_TIMEOUT(), TimeUnit.SECONDS)
                .writeTimeout(props.getWRITE_TIMEOUT(), TimeUnit.SECONDS)
                .build();
    }
}
