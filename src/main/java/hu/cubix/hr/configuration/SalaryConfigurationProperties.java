package hu.cubix.hr.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr.employees.salary")
@Component
@Getter
@Setter
public class SalaryConfigurationProperties {

    private Limit limit;
    private Percentage percentage;

    @Setter
    @Getter
    public static class Limit {
        private int firstCategory;
        private int secondCategory;
        private int thirdCategory;

    }

    @Setter
    @Getter
    public static class Percentage {
        private int firstCategory;
        private int secondCategory;
        private int thirdCategory;
        private int fourthCategory;

    }
}
