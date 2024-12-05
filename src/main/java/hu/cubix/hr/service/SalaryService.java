package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
public class SalaryService {

    private final IEmployeeService employeeService;

    public int setNewSalary(Employee employee) {
        return employee.getSalary() / 100 * (100 + employeeService.getPayRaisePercent(employee));
    }
}
