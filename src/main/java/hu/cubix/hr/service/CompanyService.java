package hu.cubix.hr.service;

import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

    private final Map<Integer, Company> companies = new HashMap<>();

    public List<Company> getAllCompanies() {
        return new ArrayList<>(companies.values());
    }

    public Company getCompanyById(int id) {
        return companies.get(id);
    }

    public Company createCompany(Company company) {
        return getCompanyById(company.getId()) == null ? saveCompany(company) : null;
    }

    public Company updateCompany(Company company) {
        return getCompanyById(company.getId()) == null ? null : saveCompany(company);
    }

    public void deleteCompanyById(int id) {
        companies.remove(id);
    }

    public Company addEmployeeToCompany(int id, Employee employee) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees().add(employee);
        return company;
    }

    public Company deleteEmployeeFromCompany(int id, int employeeId) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees()
            .stream()
            .filter(employee -> employee.getId() == employeeId)
            .findFirst()
            .ifPresent(employee -> company.getEmployees().remove(employee));
        return company;
    }

    public Company updateEmployeesOfCompany(int id, List<Employee> employees) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.setEmployees(employees);
        return company;
    }

    private Company saveCompany(Company company) {
        companies.put(company.getId(), company);
        return company;
    }
}
