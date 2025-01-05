package hu.cubix.hr.service;

import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@AllArgsConstructor
@Service
@Transactional
public class InitDbService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public void clearDb() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
    }

    public void insertTestData() {
        Company company1 = new Company(11111111, "Alfa Cég", "1118 Budapest, Alfa utca 1.");
        Company company2 = new Company(22222222, "Béta Cég", "1118 Budapest, Béta utca 2.");
        List<Employee> company1Employees =List.of(
            new Employee("A Aladár", "Alabárdos", 1000,
                LocalDateTime.of(2010, 5, 10, 10, 0, 0), company1),
            new Employee("B Béla", "Barista", 4000,
                LocalDateTime.of(2017, 4, 23, 10, 0, 0), company1),
            new Employee("C Cecil", "Cementgyáros", 6000,
                LocalDateTime.of(2023, 5, 10, 10, 0, 0), company1)
        );
        List<Employee> company2Employees =List.of(
            new Employee("F Ferenc", "Fakanálkészítő", 2000,
                LocalDateTime.of(2010, 5, 10, 10, 0, 0), company2),
            new Employee("G Gábor", "Galvanizáló", 4000,
                LocalDateTime.of(2017, 4, 23, 10, 0, 0), company2),
            new Employee("H Henrik", "Hűtőtúró", 6000,
                LocalDateTime.of(2023, 5, 10, 10, 0, 0), company2)
        );

        company1Employees.forEach(employee -> employee.setCompany(company1));
        company2Employees.forEach(employee -> employee.setCompany(company2));
        company1.setEmployees(company1Employees);
        company2.setEmployees(company2Employees);

        List<Employee> employees = Stream.of(company1Employees, company2Employees)
            .flatMap(Collection::stream)
            .toList();
        List<Company> companies = List.of(company1, company2);

        employeeRepository.saveAll(employees);
        companyRepository.saveAll(companies);
    }
}
