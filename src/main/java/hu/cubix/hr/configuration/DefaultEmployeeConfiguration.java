package hu.cubix.hr.configuration;

import hu.cubix.hr.service.DefaultEmployeeService;
import hu.cubix.hr.service.IEmployeeService;
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
