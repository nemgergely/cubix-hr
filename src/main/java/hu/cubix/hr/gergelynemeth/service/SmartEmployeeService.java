package hu.cubix.hr.gergelynemeth.service;

import hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties;
import hu.cubix.hr.gergelynemeth.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties.Limit;
import static hu.cubix.hr.gergelynemeth.configuration.SalaryConfigurationProperties.Percentage;

@Service
public class SmartEmployeeService implements IEmployeeService {

    @Autowired
    SalaryConfigurationProperties salaryConfigurationProperties;

    @Override
    public int getPayRaisePercent(Employee employee) {
        LocalDateTime timeSpentInCompany = LocalDateTime.now()
            .minusYears(employee.getJoinDateTime().getYear())
            .minusMonths(employee.getJoinDateTime().getMonthValue())
            .minusDays(employee.getJoinDateTime().getDayOfMonth())
            .minusHours(employee.getJoinDateTime().getHour())
            .minusMinutes(employee.getJoinDateTime().getMinute())
            .minusSeconds(employee.getJoinDateTime().getSecond());

        int monthsSpentInCompany = timeSpentInCompany.getYear() * 12 + timeSpentInCompany.getMonthValue();
        return calculatePercent(monthsSpentInCompany);
    }

    private int calculatePercent(int monthsSpentInCompany) {
        Limit limit = salaryConfigurationProperties.getLimit();
        Percentage percentage = salaryConfigurationProperties.getPercentage();

        if (monthsSpentInCompany >= limit.getFirstCategory()) {
            return percentage.getFirstCategory();
        } else if (monthsSpentInCompany >= limit.getSecondCategory()) {
            return percentage.getSecondCategory();
        } else if (monthsSpentInCompany >= limit.getThirdCategory()) {
            return percentage.getThirdCategory();
        } else {
            return percentage.getFourthCategory();
        }
    }
}
