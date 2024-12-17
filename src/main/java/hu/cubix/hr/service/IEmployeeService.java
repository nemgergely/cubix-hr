package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;

import java.time.LocalDateTime;
import java.util.List;

public interface IEmployeeService {

    int getPayRaisePercent(Employee employee);

    List<Employee> getAllEmployees();

    List<Employee> getRichEmployees(int salary);

    Employee getEmployeeById(int id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployeeById(int id);

    List<Employee> findAllEmployeesByJob(String job);

    List<Employee> findAllEmployeesWithNamePrefix(String namePrefix);

    List<Employee> findAllEmployeesByJoinTimeFrame(LocalDateTime from, LocalDateTime to);
}
