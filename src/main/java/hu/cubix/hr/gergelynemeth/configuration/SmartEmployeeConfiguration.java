package hu.cubix.hr.gergelynemeth.configuration;

import hu.cubix.hr.gergelynemeth.service.IEmployeeService;
import hu.cubix.hr.gergelynemeth.service.SmartEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("smart")
public class SmartEmployeeConfiguration {

    @Bean
    public IEmployeeService employeeService() {
        return new SmartEmployeeService();
    }
}
