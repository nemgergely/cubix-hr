package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService {

    @Override
    public int getPayRaisePercent(Employee employee) {
        return 5;
    }
}
