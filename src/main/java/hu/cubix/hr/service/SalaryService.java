package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.EmployeeRepository;
import hu.cubix.hr.repository.PositionDetailsByCompanyRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@AllArgsConstructor
public class SalaryService {

    private final IEmployeeService employeeService;
    private final PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;
    private final EmployeeRepository employeeRepository;

    public int setNewSalary(Employee employee) {
        return employee.getSalary() / 100 * (100 + employeeService.getPayRaisePercent(employee));
    }

    @Transactional
    public void raiseMinSalary(Integer companyId, String jobTitle, int minSalary) {

        positionDetailsByCompanyRepository.findByPositionJobTitleAndCompanyId(jobTitle, companyId)
            .forEach(pd -> {
                pd.setMinSalary(minSalary);
                employeeRepository.updateSalaries(companyId, jobTitle, minSalary);
            });
    }
}
