package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IEmployeeService {

    int getPayRaisePercent(Employee employee);

    List<Employee> getAllEmployees();

    Page<Employee> getRichEmployees(int salary, Pageable pageable);

    Employee getEmployeeById(int id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployeeById(int id);

    List<Employee> findAllEmployeesByJob(String job);

    List<Employee> findAllEmployeesWithNamePrefix(String prefix);

    List<Employee> findAllEmployeesByJoinTimeFrame(LocalDateTime from, LocalDateTime to);
}
