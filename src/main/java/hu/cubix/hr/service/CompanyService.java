package hu.cubix.hr.service;

import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company createCompany(Company company) {
        company.getEmployees().forEach(employee -> employee.setCompany(company));
        return companyRepository.save(company);
    }

    public Company updateCompany(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            return null;
        }
        return companyRepository.save(company);
    }

    public void deleteCompanyById(int id) {
        companyRepository.deleteById(id);
    }

    public Company addEmployeeToCompany(int id, Employee employee) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees().add(employee);
        return companyRepository.save(company);
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
        return companyRepository.save(company);
    }

    public Company updateEmployeesOfCompany(int id, List<Employee> employees) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.setEmployees(employees);
        return companyRepository.save(company);
    }
}
