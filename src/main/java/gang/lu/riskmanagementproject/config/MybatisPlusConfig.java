package gang.lu.riskmanagementproject.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import gang.lu.riskmanagementproject.property.PageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 21:02
 * @description MP配置
 */
@Configuration
@RequiredArgsConstructor
public class MybatisPlusConfig {

    private final PageProperty pageProperty;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型
        paginationInterceptor.setDbType(DbType.MYSQL);
        // 溢出总页数后自动处理（默认返回空）
        paginationInterceptor.setOverflow(false);
        // 设置单页最大限制
        paginationInterceptor.setMaxLimit(Long.valueOf(pageProperty.getMAX_SIZE()));
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}