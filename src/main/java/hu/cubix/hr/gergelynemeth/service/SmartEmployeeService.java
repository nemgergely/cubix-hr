package hu.cubix.hr.gergelynemeth.service;

import hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties;
import hu.cubix.hr.gergelynemeth.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties.Limit;
import static hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties.Percentage;

@Service
public class SmartEmployeeService implements IEmployeeService {

    @Autowired
    SalaryConfigurationProperties salaryConfigurationProperties;

    @Override
    public int getPayRaisePercent(Employee employee) {

        float yearsSpentInCompany = (float) (ChronoUnit.MONTHS.between(
            employee.getJoinDateTime(), LocalDateTime.now()) / 12.0);

        return calculatePercent(yearsSpentInCompany);
    }

    private int calculatePercent(float yearsSpentInCompany) {
        Limit limit = salaryConfigurationProperties.getLimit();
        Percentage percentage = salaryConfigurationProperties.getPercentage();

        if (yearsSpentInCompany >= limit.getFirstCategory()) {
            return percentage.getFirstCategory();
        } else if (yearsSpentInCompany >= limit.getSecondCategory()) {
            return percentage.getSecondCategory();
        } else if (yearsSpentInCompany >= limit.getThirdCategory()) {
            return percentage.getThirdCategory();
        } else {
            return percentage.getFourthCategory();
        }
    }
}
