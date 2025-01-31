package hu.cubix.hr.service;

import hu.cubix.hr.model.AverageSalaryByPosition;
import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public List<Company> getAllCompanies(Optional<Boolean> full) {
        return full.isPresent() && Boolean.TRUE.equals(full.get()) ?
            companyRepository.findAllWithEmployees() :
            companyRepository.findAll();
    }

    public Company getCompanyWithEmployeesById(int id) {
        return companyRepository.findByIdWithEmployees(id).orElse(null);
    }

    public Company getCompanyById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Transactional
    public Company createCompany(Company company) {
        company.getEmployees().forEach(employee -> employee.setCompany(company));
        employeeRepository.saveAll(company.getEmployees());
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            return null;
        }
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompanyById(int id) {
        companyRepository.deleteById(id);
    }

    @Transactional
    public Company addEmployeeToCompany(int id, Employee employee) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        company.getEmployees().add(employee);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return companyRepository.save(company);
    }

    @Transactional
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
        employeeRepository.deleteById(employeeId);
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateEmployeesOfCompany(int id, List<Employee> employees) {
        Company company = getCompanyById(id);
        if (company == null) {
            return null;
        }
        employeeRepository.deleteByCompanyId(id);
        employees.forEach(employee -> employee.setCompany(company));
        company.setEmployees(employees);
        employeeRepository.saveAll(employees);
        // Szerintem azért lehet elhagyni a save() utasítást, mert a tranzakció végén
        // minden, a managed entitásokon végzett változtatás automatikusan lementődik,
        // és egy saveAndFlush() hívódik meg
        return company;
        // return companyRepository.save(company);
    }

    public List<Company> findCompaniesByHavingEmployeeWithSalaryAboveGiven(Integer minSalary, Optional<Boolean> full) {
        return full.isPresent() && Boolean.TRUE.equals(full.get()) ?
            companyRepository.findAllWithEmployeesByHavingEmployeeWithSalaryAboveGiven(minSalary) :
            companyRepository.findAllByHavingEmployeeWithSalaryAboveGiven(minSalary);
    }

    public List<Company> findCompaniesWithMoreEmployeesThanGiven(Integer employeeLimit, Optional<Boolean> full) {
        return full.isPresent() && Boolean.TRUE.equals(full.get()) ?
            companyRepository.findAllWithEmployeesWithMoreEmployeesThanGiven(employeeLimit) :
            companyRepository.findAllWithMoreEmployeesThanGiven(employeeLimit);
    }

    public List<AverageSalaryByPosition> findAverageSalariesByPosition(Integer companyId) {
        return companyRepository.findAverageSalariesByPosition(companyId);
    }
}
