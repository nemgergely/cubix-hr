package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntTest {

    @Autowired
    WebTestClient webTestClient;

    private final List<EmployeeDto> initialEmployees = List.of(
        new EmployeeDto(1, "A Aladar", 1000,
            LocalDateTime.of(1990, 11, 8, 18, 0, 0)),
        new EmployeeDto(2, "B Bela", 2000,
            LocalDateTime.of(2000, 11, 8, 18, 0, 0)),
        new EmployeeDto(3, "C Cecil", 3000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0)),
        new EmployeeDto(4, "D Denes", 4000,
            LocalDateTime.of(2015, 11, 8, 18, 0, 0)),
        new EmployeeDto(5, "E Elemer", 5000,
            LocalDateTime.of(2020, 11, 8, 18, 0, 0))
    );

    public static final String API_EMPLOYEES = "/api/employees";

    @BeforeEach
    void setUp() {
        initialEmployees.forEach(employee -> createEmployeePostRequest(employee, true));
    }

    @AfterEach
    void tearDown() {
        initialEmployees.forEach(
            employeeDto -> webTestClient
                .delete()
                .uri(API_EMPLOYEES.concat("//").concat(String.valueOf(employeeDto.id())))
                .exchange());
    }

    @Test
    void testValidCreateEmployee() {
        Integer id = 6;
        if (isValidIdForRequest(id, false)) {
            EmployeeDto newEmployee = new EmployeeDto(
                id, "F Ferenc", 1000,
                LocalDateTime.of(2012, 5, 22, 6, 0, 0));
            List<EmployeeDto> employeesBeforeRequest = getAllEmployees();

            createEmployeePostRequest(newEmployee, true);

            List<EmployeeDto> employeesAfterRequest = getAllEmployees();
            assertThat(employeesAfterRequest.subList(0, employeesBeforeRequest.size()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(employeesBeforeRequest);
            assertThat(employeesAfterRequest.get(employeesAfterRequest.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(newEmployee);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "7, G Gabor, -20, 2017-05-22T10:00:00",
        "8, Christopher Lloyd (Doki), 6000, 2035-05-22T18:00:00",
        "9, null, 10000, 2023-05-22T22:00:00"
    }, nullValues = "null")
    void testInvalidCreateEmployee(Integer id, String name, int salary, String joinDateTimeString) {
        LocalDateTime joinDateTime = LocalDateTime.parse(joinDateTimeString);
        if (isValidIdForRequest(id, false)) {
            List<EmployeeDto> employeesBeforeRequest = getAllEmployees();
            EmployeeDto newEmployee = new EmployeeDto(id, name, salary, joinDateTime);

            createEmployeePostRequest(newEmployee, false);

            List<EmployeeDto> employeesAfterRequest = getAllEmployees();
            assertThat(employeesAfterRequest)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(employeesBeforeRequest);
        }
    }

    @Test
    void testValidUpdateEmployee() {
        Integer id = 1;
        if (isValidIdForRequest(id, true)) {
            EmployeeDto employeeForUpdate = new EmployeeDto(
                id, "A Aladar Uj", 1000,
                LocalDateTime.of(2012, 5, 22, 6, 0, 0));
            List<EmployeeDto> employeesBeforeRequest = getAllEmployees();

            updateEmployeePutRequest(employeeForUpdate, true);

            List<EmployeeDto> employeesAfterRequest = getAllEmployees();
            EmployeeDto originalDto = employeesBeforeRequest
                .stream().filter(e -> e.id().equals(id)).findFirst().get();
            EmployeeDto updatedDto = employeesAfterRequest
                .stream().filter(e -> e.id().equals(id)).findFirst().get();
            assertEquals(originalDto.id(), updatedDto.id());
            assertThat(employeeForUpdate)
                .usingRecursiveComparison()
                .isEqualTo(updatedDto);
            assertEquals(employeesBeforeRequest.size(), employeesAfterRequest.size());
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "2, B Bela Uj, 0, 2017-05-22T10:00:00",
        "3, Christopher Lloyd Uj, 6000, 2035-05-22T18:00:00",
        "4, null, 10000, 2023-05-22T22:00:00"
    }, nullValues = "null")
    void testInvalidUpdateEmployee(int id, String name, int salary, String joinDateTimeString) {
        LocalDateTime joinDateTime = LocalDateTime.parse(joinDateTimeString);
        if (isValidIdForRequest(id, true)) {
            List<EmployeeDto> employeesBeforeRequest = getAllEmployees();
            EmployeeDto employeeForUpdate = new EmployeeDto(id, name, salary, joinDateTime);

            updateEmployeePutRequest(employeeForUpdate, false);

            List<EmployeeDto> employeesAfterRequest = getAllEmployees();
            assertThat(employeesAfterRequest)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(employeesBeforeRequest);
        }
    }

    private void createEmployeePostRequest(EmployeeDto newEmployee, boolean isValidRequest) {
        if (isValidRequest) {
            webTestClient
                .post()
                .uri(API_EMPLOYEES)
                .bodyValue(newEmployee)
                .exchange()
                .expectStatus()
                .isOk();
        } else {
            webTestClient
                .post()
                .uri(API_EMPLOYEES)
                .bodyValue(newEmployee)
                .exchange()
                .expectStatus()
                .isBadRequest();
        }
    }

    private void updateEmployeePutRequest(EmployeeDto employeeForUpdate, boolean isValidRequest) {
        if (isValidRequest) {
            webTestClient
                .put()
                .uri(API_EMPLOYEES)
                .bodyValue(employeeForUpdate)
                .exchange()
                .expectStatus()
                .isOk();
        } else {
            webTestClient
                .put()
                .uri(API_EMPLOYEES)
                .bodyValue(employeeForUpdate)
                .exchange()
                .expectStatus()
                .isBadRequest();
        }
    }

    private List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> allEmployees = webTestClient
            .get()
            .uri(API_EMPLOYEES)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeDto.class)
            .returnResult()
            .getResponseBody();

        allEmployees.sort(Comparator.comparing(EmployeeDto::id));
        return allEmployees;
    }

    private boolean isValidIdForRequest(Integer id, boolean isUpdateRequest) {
        List<EmployeeDto> employees = getAllEmployees();
        return isUpdateRequest ?
            employees
                .stream()
                .anyMatch(employeeDto -> employeeDto.id().equals(id)) :
            employees
                .stream()
                .noneMatch(employeeDto -> employeeDto.id().equals(id));
    }
}
