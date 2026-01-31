package gang.lu.riskmanagementproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("gang.lu.riskmanagementproject.mapper")
public class RiskManagementProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskManagementProjectApplication.class, args);
    }

}
