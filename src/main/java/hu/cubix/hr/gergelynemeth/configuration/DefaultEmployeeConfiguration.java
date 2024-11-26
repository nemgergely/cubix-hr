package hu.cubix.hr.gergelynemeth.configuration;

import hu.cubix.hr.gergelynemeth.service.DefaultEmployeeService;
import hu.cubix.hr.gergelynemeth.service.IEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!smart")
public class DefaultEmployeeConfiguration {

    @Bean
    public IEmployeeService employeeService() {
        return new DefaultEmployeeService();
    }
}
