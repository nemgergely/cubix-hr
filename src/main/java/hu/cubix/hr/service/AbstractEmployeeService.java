package hu.cubix.hr.service;

import hu.cubix.hr.model.AverageSalaryByPosition;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.EmployeeRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Setter(onMethod_ = {@Autowired})
@Transactional
public abstract class AbstractEmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> getRichEmployees(int salary, Pageable pageable) {
        return employeeRepository.findBySalaryGreaterThan(salary, pageable);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsById(employee.getId())) {
            return null;
        }
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> findAllEmployeesByJob(String job) {
        return employeeRepository.findByPositionJobTitle(job);
    }

    @Override
    public List<Employee> findAllEmployeesWithNamePrefix(String prefix) {
        return employeeRepository.findByNameStartingWithIgnoreCase(prefix);
    }

    @Override
    public List<Employee> findAllEmployeesByJoinTimeFrame(LocalDateTime from, LocalDateTime to) {
        return employeeRepository.findByJoinDateTimeBetween(from, to);
    }
}
