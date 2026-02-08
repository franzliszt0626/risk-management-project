package gang.lu.riskmanagementproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author Franz Liszt
 * @date 2026-02-01 11:07:06
 * @description 主启动类
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("gang.lu.riskmanagementproject.mapper")
public class RiskManagementProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskManagementProjectApplication.class, args);
    }

}
