package hu.cubix.hr.gergelynemeth.service;

import hu.cubix.hr.gergelynemeth.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService implements IEmployeeService {

    @Override
    public int getPayRaisePercent(Employee employee) {
        return 5;
    }
}
