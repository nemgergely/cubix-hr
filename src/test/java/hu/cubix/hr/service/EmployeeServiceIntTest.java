package hu.cubix.hr.service;

import hu.cubix.hr.enums.Qualification;
import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.model.Position;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import hu.cubix.hr.repository.PositionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class EmployeeServiceIntTest {

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    CompanyRepository companyRepository;

    @BeforeEach
    public void setUp() {
        prepareEntities();
    }

    @AfterEach
    public void tearDown() {
        companyRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "A Aladár test case, " +
            "null, A Aladár, 10000, 2015-01-21T08:00:00, " +
            "11111111, AAA cég, Budapest AAA utca 1., " +
            "Asztaltisztító, HIGH_SCHOOL",
        "B Béla test case, " +
            "null, B Béla, 10000, 2020-01-21T08:00:00, " +
            "11111111, AAA cég, Budapest AAA utca 1., " +
            "Borvedelő, NONE",
        "C Cecil test case, " +
            "null, C Cecil, 50000, 2015-01-21T08:00:00, " +
            "22222222, BBB cég, Budapest BBB utca 2., " +
            "Asztaltisztító, HIGH_SCHOOL",
        "D Dénes test case, " +
            "null, D Dénes, 50000, 2020-01-21T08:00:00, " +
            "22222222, BBB cég, Budapest BBB utca 2., " +
            "Borvedelő, NONE",
        "All in AAA company, " +
            "null, null, null, null, " +
            "11111111, AAA, Budapest AAA utca 1., " +
            "null, null",
        "All in BBB company, " +
            "null, null, null, null, " +
            "22222222, BBB, Budapest BBB utca 2., " +
            "null, null",
        "All with salary in 5% range from 80000, " +
            "null, null, 80000, null, " +
            "0, null, null, " +
            "null, null",
        "All with join date in 2015-01-21, " +
            "null, null, null, 2015-01-21T08:00:00, " +
            "0, null, null, " +
            "null, null",
        "All with name starting with A and company name starting with AAA, " +
            "null, A, null, null, " +
            "22222222, AAA, Budapest AAA utca 1., " +
            "null, null",
        "All in Borvedelő position, " +
            "null, null, null, null, " +
            "0, null, null, " +
            "Borvedelő, NONE",
    }, nullValues = "null")
    void testFindingEmployeesByExample(
        String caseName,
        Integer id,
        String name,
        Integer salary,
        String joinDateTimeString,
        int registrationNumber,
        String companyName,
        String address,
        String jobTitle,
        String qualification
    ) {
        // ARRANGE
        LocalDateTime joinDateTime = StringUtils.hasLength(joinDateTimeString) ?
            LocalDateTime.parse(joinDateTimeString) : null;
        Company exampleCompany = null;
        if (companyName != null && address != null && registrationNumber != 0) {
            exampleCompany = new Company(registrationNumber, companyName, address);
        }
        Position examplePosition = null;
        if (joinDateTime != null && qualification != null) {
            examplePosition = new Position(jobTitle, Qualification.valueOf(qualification));
        }
        Employee exampleEmployee = new Employee(id, name, salary, joinDateTime, exampleCompany, examplePosition);

        // ACT
        List<Employee> employeesByExample = employeeService.findEmployeesByExample(exampleEmployee)
            .stream()
            .sorted(Comparator.comparing(Employee::getName))
            .toList();

        // ASSERT
        switch (caseName) {
            case "A Aladár test case":
                assertEquals(1, employeesByExample.size());
                assertEquals("A Aladár", employeesByExample.get(0).getName());
                break;
            case "B Béla test case":
                assertEquals(1, employeesByExample.size());
                assertEquals("B Béla", employeesByExample.get(0).getName());
                break;
            case "C Cecil test case":
                assertEquals(1, employeesByExample.size());
                assertEquals("C Cecil", employeesByExample.get(0).getName());
                break;
            case "D Dénes test case":
                assertEquals(1, employeesByExample.size());
                assertEquals("D Dénes", employeesByExample.get(0).getName());
                break;
            case "All in AAA company":
                assertEquals(3, employeesByExample.size());
                assertEquals("A Aladár", employeesByExample.get(0).getName());
                assertEquals("A Elemér", employeesByExample.get(1).getName());
                assertEquals("B Béla", employeesByExample.get(2).getName());
                break;
            case "All in BBB company":
                assertEquals(3, employeesByExample.size());
                assertEquals("B Ferenc", employeesByExample.get(0).getName());
                assertEquals("C Cecil", employeesByExample.get(1).getName());
                assertEquals("D Dénes", employeesByExample.get(2).getName());
                break;
            case "All with salary in 5% range from 80000":
                assertEquals(2, employeesByExample.size());
                assertEquals("A Elemér", employeesByExample.get(0).getName());
                assertEquals("B Ferenc", employeesByExample.get(1).getName());
                break;
            case "All with join date in 2015-01-21":
                assertEquals(3, employeesByExample.size());
                assertEquals("A Aladár", employeesByExample.get(0).getName());
                assertEquals("A Elemér", employeesByExample.get(1).getName());
                assertEquals("C Cecil", employeesByExample.get(2).getName());
                break;
            case "All with name starting with A and company name starting with AAA":
                assertEquals(2, employeesByExample.size());
                assertEquals("A Aladár", employeesByExample.get(0).getName());
                assertEquals("A Elemér", employeesByExample.get(1).getName());
                break;
            case "All in Bárpultos position":
                assertEquals(3, employeesByExample.size());
                assertEquals("B Béla", employeesByExample.get(0).getName());
                assertEquals("B Ferenc", employeesByExample.get(1).getName());
                assertEquals("D Dénes", employeesByExample.get(2).getName());
                break;
            default:
                break;
        }
    }

    private void prepareEntities() {
        Company company1 = new Company(11111111, "AAA Cég", "Budapest AAA utca 1.");
        Company company2 = new Company(22222222, "BBB Cég", "Budapest BBB utca 2.");
        Position position1 = new Position("Asztaltisztító", Qualification.HIGH_SCHOOL);
        Position position2 = new Position("Borvedelő", Qualification.NONE);
        Employee employee1 = new Employee("A Aladár", 10000,
            LocalDateTime.of(2015, 1, 21, 8, 0, 0));
        Employee employee2 = new Employee("B Béla", 10000,
            LocalDateTime.of(2020, 1, 21, 8, 0, 0));
        Employee employee3 = new Employee("C Cecil", 50000,
            LocalDateTime.of(2015, 1, 21, 8, 0, 0));
        Employee employee4 = new Employee("D Dénes", 50000,
            LocalDateTime.of(2020, 1, 21, 8, 0, 0));
        Employee employee5 = new Employee("A Elemér", 79000,
            LocalDateTime.of(2015, 1, 21, 8, 0, 0));
        Employee employee6 = new Employee("B Ferenc", 81000,
            LocalDateTime.of(2020, 1, 21, 8, 0, 0));

        position1.setEmployees(List.of(employee1, employee3, employee5));
        position2.setEmployees(List.of(employee2, employee4, employee6));
        positionRepository.saveAll(List.of(position1, position2));

        company1.setEmployees(List.of(employee1, employee2, employee5));
        company2.setEmployees(List.of(employee3, employee4, employee6));
        companyRepository.saveAll(List.of(company1, company2));

        employee1.setPosition(position1);
        employee2.setPosition(position2);
        employee3.setPosition(position1);
        employee4.setPosition(position2);
        employee5.setPosition(position1);
        employee6.setPosition(position2);
        employee1.setCompany(company1);
        employee2.setCompany(company1);
        employee5.setCompany(company1);
        employee3.setCompany(company2);
        employee4.setCompany(company2);
        employee6.setCompany(company2);
        employeeRepository.saveAll(List.of(employee1, employee2, employee3, employee4, employee5, employee6));
    }
}
