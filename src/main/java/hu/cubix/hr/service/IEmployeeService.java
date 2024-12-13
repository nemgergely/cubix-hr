package hu.cubix.hr.service;

import hu.cubix.hr.model.Employee;

import java.util.List;

public interface IEmployeeService {

    int getPayRaisePercent(Employee employee);

    List<Employee> getAllEmployees();

    List<Employee> getRichEmployees(int salary);

    Employee getEmployeeById(int id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployeeById(int id);
}
