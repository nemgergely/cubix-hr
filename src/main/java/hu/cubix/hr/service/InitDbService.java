package hu.cubix.hr.service;

import hu.cubix.hr.enums.Qualification;
import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.model.Position;
import hu.cubix.hr.model.PositionDetailsByCompany;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import hu.cubix.hr.repository.PositionDetailsByCompanyRepository;
import hu.cubix.hr.repository.PositionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Service
@Transactional
public class InitDbService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

    public void clearDb() {
        companyRepository.deleteAll();
        positionRepository.deleteAll();
    }

    public void insertTestData() {
        Position carpenter = positionRepository.save(new Position("Asztalos", Qualification.UNIVERSITY));
        Position bartender = positionRepository.save(new Position("Bárpultos", Qualification.HIGH_SCHOOL));

        Employee newEmployee1 = employeeRepository.save(new Employee("A Aladár", 200000, LocalDateTime.now()));
        newEmployee1.setPosition(carpenter);

        Employee newEmployee2 = employeeRepository.save(new Employee("B Béla", 200000, LocalDateTime.now()));
        newEmployee2.setPosition(bartender);

        Company newCompany = companyRepository.save(
            new Company(11111111, "Alfa Cég", "Budapest, Alfa utca 6."));
        newCompany.addEmployee(newEmployee2);
        newCompany.addEmployee(newEmployee1);

        PositionDetailsByCompany pd = new PositionDetailsByCompany();
        pd.setCompany(newCompany);
        pd.setMinSalary(250000);
        pd.setPosition(carpenter);
        positionDetailsByCompanyRepository.save(pd);

        PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
        pd2.setCompany(newCompany);
        pd2.setMinSalary(200000);
        pd2.setPosition(bartender);
        positionDetailsByCompanyRepository.save(pd2);
    }
}
