package hu.cubix.hr.service;

import hu.cubix.hr.dto.EmployeeDto;
import hu.cubix.hr.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEmployeeService implements IEmployeeService {

    private final Map<Integer, Employee> employees = new HashMap<>();

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public List<Employee> getRichEmployees(int salary) {
        return employees.values()
            .stream()
            .filter( employee -> employee.getSalary() > salary)
            .toList();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if (getEmployeeById(employee.getId()) != null) {
            return null;
        }
        return saveEmployee(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (getEmployeeById(employee.getId()) == null) {
            return null;
        }
        return saveEmployee(employee);
    }

    @Override
    public void deleteEmployeeById(int id) {
        employees.remove(id);
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employees.get(id);
    }

    private Employee saveEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }
}
