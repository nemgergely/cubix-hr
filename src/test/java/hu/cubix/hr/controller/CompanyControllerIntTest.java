package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import hu.cubix.hr.mapper.IEmployeeMapper;
import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.repository.CompanyRepository;
import hu.cubix.hr.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyControllerIntTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    IEmployeeMapper employeeMapper;

    public static final String API_COMPANIES = "/api/companies";

    private final List<Employee> initialEmployees = List.of(
        new Employee(null, "A Aladar", 1000,
            LocalDateTime.of(1990, 11, 8, 18, 0, 0)),
        new Employee(null, "B Bela", 2000,
            LocalDateTime.of(2000, 11, 8, 18, 0, 0))
    );

    private final Company initialCompany =
        new Company(null, 11111111, "Alfa Cég", "Budapest, Alfa utca 6.");

    @BeforeEach
    void setUp() {
        initialCompany.setEmployees(initialEmployees);
        companyRepository.save(initialCompany);
        initialEmployees.forEach( employee -> employee.setCompany(initialCompany));
        employeeRepository.saveAll(initialEmployees);
    }

    @AfterEach
    void tearDown() {
        // this call should suffice as I use CascadeType.REMOVE on employee child entities
        companyRepository.deleteAll();
    }

    @Test
    void testAddEmployeeToCompany() {
        // ARRANGE
        Company companyBeforeAdding = companyRepository.findAllWithEmployees().get(0);
        List<Employee> employeesBeforeAdding = sortEmployeesById(companyBeforeAdding.getEmployees());
        EmployeeDto employeeDto = new EmployeeDto(null, "C Cecil", 4000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0));

        // ACT
        addEmployeePostRequest(companyBeforeAdding.getId(), employeeDto);

        // ASSERT
        Company companyAfterAdding = companyRepository.findAllWithEmployees().get(0);
        List<Employee> employeesAfterAdding = sortEmployeesById(companyAfterAdding.getEmployees());

        assertEquals(companyBeforeAdding.getId(), companyAfterAdding.getId());
        assertThat(employeesAfterAdding.subList(0, employeesBeforeAdding.size()))
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("company")
            .containsExactlyElementsOf(employeesBeforeAdding);
        assertEquals(3, employeesAfterAdding.size());
        assertThat(employeeMapper.employeeToDto(employeesAfterAdding.get(2)))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(employeeDto);
    }

    @Test
    void testUpdateEmployeesOfCompany() {
        // ARRANGE
        Company companyBeforeEmployeeUpdate = companyRepository.findAllWithEmployees().get(0);
        List<Employee> oldEmployees = sortEmployeesById(companyBeforeEmployeeUpdate.getEmployees());
        List<EmployeeDto> employeeDtos = getEmployeeDtos();

        // ACT
        updateEmployeesPutRequest(companyBeforeEmployeeUpdate.getId(), employeeDtos);

        // ASSERT
        Company companyAfterEmployeeUpdate = companyRepository.findAllWithEmployees().get(0);
        List<Employee> newEmployees = sortEmployeesById(companyAfterEmployeeUpdate.getEmployees());

        assertEquals(companyBeforeEmployeeUpdate.getId(), companyAfterEmployeeUpdate.getId());
        assertTrue(newEmployees.stream().noneMatch(oldEmployees::contains));
        assertEquals(4, newEmployees.size());
        assertThat(employeeMapper.employeesToDtos(newEmployees))
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "company")
            .containsExactlyElementsOf(employeeDtos);
    }

    @Test
    void testDeleteEmployeeFromCompany() {
        // ARRANGE
        Company companyBeforeEmployeeDelete = companyRepository.findAllWithEmployees().get(0);
        List<Employee> originalEmployees = sortEmployeesById(companyBeforeEmployeeDelete.getEmployees());
        Integer employeeIdToRemove = originalEmployees.get(originalEmployees.size() - 1).getId();

        // ACT
        removeEmployeeDeleteRequest(companyBeforeEmployeeDelete.getId(), employeeIdToRemove);

        // ASSERT
        Company companyAfterEmployeeUpdate = companyRepository.findAllWithEmployees().get(0);
        List<Employee> remainingEmployees = sortEmployeesById(companyAfterEmployeeUpdate.getEmployees());
        Employee removedEmployee = employeeRepository.findById(employeeIdToRemove).orElse(null);

        if (removedEmployee != null) {
            assertEquals(companyBeforeEmployeeDelete.getId(), companyAfterEmployeeUpdate.getId());
            assertTrue(remainingEmployees.stream().noneMatch(
                remainingEmployee -> remainingEmployee.equals(removedEmployee)));
            assertEquals(originalEmployees.size() - 1, remainingEmployees.size());
        }
    }

    private void addEmployeePostRequest(Integer companyId, EmployeeDto employeeDto) {
        webTestClient
            .post()
            .uri(API_COMPANIES
                .concat("//")
                .concat(String.valueOf(companyId))
                .concat("//")
                .concat("addEmployee"))
            .bodyValue(employeeDto)
            .exchange()
            .expectStatus()
            .isOk();
    }

    private void updateEmployeesPutRequest(Integer companyId, List<EmployeeDto> employeeDtos) {
        webTestClient
            .put()
            .uri(API_COMPANIES
                .concat("//")
                .concat(String.valueOf(companyId))
                .concat("//")
                .concat("updateEmployees"))
            .bodyValue(employeeDtos)
            .exchange()
            .expectStatus()
            .isOk();
    }

    private void removeEmployeeDeleteRequest(Integer companyId, Integer employeeIdToDelete) {
        webTestClient
            .delete()
            .uri(API_COMPANIES
                .concat("//")
                .concat(String.valueOf(companyId))
                .concat("//")
                .concat("deleteEmployee")
                .concat("//")
                .concat(String.valueOf(employeeIdToDelete)))
            .exchange()
            .expectStatus()
            .isOk();
    }

    private List<Employee> sortEmployeesById(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparing(Employee::getId)).toList();
    }


    private List<EmployeeDto> getEmployeeDtos() {
        EmployeeDto employeeDto1 = new EmployeeDto(null, "C Cecil", 4000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0));
        EmployeeDto employeeDto2 = new EmployeeDto(null, "D Denes", 6000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0));
        EmployeeDto employeeDto3 = new EmployeeDto(null, "E Elemer", 8000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0));
        EmployeeDto employeeDto4 = new EmployeeDto(null, "F Ferenc", 10000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0));
        return List.of(employeeDto1, employeeDto2, employeeDto3, employeeDto4);
    }
}
