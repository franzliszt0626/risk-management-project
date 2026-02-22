package gang.lu.riskmanagementproject.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 19:58
 * @description 分页配置类
 */
@Configuration
public class PageProperty {
    public static Integer DEFAULT_NUM;
    public static Integer DEFAULT_SIZE;
    public static Integer MAX_SIZE;

    @Value("${spring.page.default-num:1}")
    public void setDefaultNum(Integer defaultNum) {
        PageProperty.DEFAULT_NUM = defaultNum;
    }

    @Value("${spring.page.default-size:10}")
    public void setDefaultSize(Integer defaultSize) {
        PageProperty.DEFAULT_SIZE = defaultSize;
    }

    @Value("${spring.page.max-size:100}")
    public void setMaxSize(Integer maxSize) {
        PageProperty.MAX_SIZE = maxSize;
    }
}