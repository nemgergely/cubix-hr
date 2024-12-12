package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    private final IEmployeeService employeeService;

    @Autowired
    public SalaryService(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public int setNewSalary(Employee employee) {
        return employee.getSalary() / 100 * (100 + employeeService.getPayRaisePercent(employee));
    }

    public IEmployeeService getEmployeeService() {
        return employeeService;
    }
}
