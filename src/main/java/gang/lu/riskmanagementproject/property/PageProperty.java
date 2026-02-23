package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 19:58
 * @description 分页配置类
 */
@Data
@Configuration
public class PageProperty {

    /**
     * 全局默认初始页码
     */
    @Value("${spring.page.default-num:1}")
    public Integer DEFAULT_NUM;

    /**
     * 全局默认单页数据量
     */
    @Value("${spring.page.default-size:10}")
    public Integer DEFAULT_SIZE;

    /**
     * 全局默认单页最大数据量
     */
    @Value("${spring.page.max-size:100}")
    public Integer MAX_SIZE;

}