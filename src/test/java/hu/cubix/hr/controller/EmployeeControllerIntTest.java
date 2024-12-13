package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

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
        new EmployeeDto(1, "A Aladar", "Alabardos", 1000,
            LocalDateTime.of(1990, 11, 8, 18, 0, 0)),
        new EmployeeDto(2, "B Bela", "Barista", 2000,
            LocalDateTime.of(2000, 11, 8, 18, 0, 0)),
        new EmployeeDto(3, "C Cecil", "Cementgyaros", 3000,
            LocalDateTime.of(2010, 11, 8, 18, 0, 0)),
        new EmployeeDto(4, "D Denes", "Darukezelo", 4000,
            LocalDateTime.of(2015, 11, 8, 18, 0, 0)),
        new EmployeeDto(5, "E Elemer", "Erdomernok", 5000,
            LocalDateTime.of(2020, 11, 8, 18, 0, 0))
    );

    public static final String API_EMPLOYEES = "/api/employees";

    @BeforeEach
    void setUp() {
        initialEmployees.forEach(this::createValidEmployee);
    }

    @AfterEach
    void tearDown() {
        initialEmployees.forEach(
            employeeDto -> webTestClient
                .delete()
                .uri(API_EMPLOYEES.concat("//").concat(String.valueOf(employeeDto.id())))
                .exchange());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "6, F Ferenc, Forradalmar, 1000, 2012-05-22T06:00:00",
        "7, G Gabor, Galvanizator, -20, 2017-05-22T10:00:00",
        "8, H Henrik, null, 4000, 2020-05-22T12:00:00",
        "9, Christopher Lloyd (Doki), Idoutazo, 6000, 2035-05-22T18:00:00",
        "10, null, Jegtoro, 10000, 2023-05-22T22:00:00"
    }, nullValues = "null")
    void testCreateEmployee(int id, String name, String job, int salary, String joinDateTimeString) {
        LocalDateTime joinDateTime = LocalDateTime.parse(joinDateTimeString);
        List<EmployeeDto> originalEmployees = getAllEmployees();
        boolean isValidId = originalEmployees
            .stream()
            .noneMatch(employeeDto -> employeeDto.id() == id);
        boolean isValidRequest = isValidId && !StringUtils.isBlank(name) && !StringUtils.isBlank(job)
            && salary > 0 && !joinDateTime.isAfter(LocalDateTime.now());
        EmployeeDto newEmployee = null;

        if (isValidRequest) {
            newEmployee = new EmployeeDto(id, name, job, salary, joinDateTime);
            createValidEmployee(newEmployee);
        } else {
            webTestClient.post().uri(API_EMPLOYEES).exchange().expectStatus().isBadRequest();
        }

        List<EmployeeDto> employeesAfterRequest = getAllEmployees();
        if (isValidRequest) {
            assertThat(employeesAfterRequest.subList(0, originalEmployees.size()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(originalEmployees);
            assertThat(employeesAfterRequest.get(employeesAfterRequest.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(newEmployee);
        } else {
            assertThat(employeesAfterRequest)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(originalEmployees);
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, A Aladar Uj, Alabardos Uj, 1000, 2012-05-22T06:00:00",
        "2, B Bela Uj, Barista Uj, 0, 2017-05-22T10:00:00",
        "3, C Cecil Uj, null, 4000, 2020-05-22T12:00:00",
        "4, Christopher Lloyd Uj, Doki Uj, 6000, 2035-05-22T18:00:00",
        "5, null, Eliminator Uj, 10000, 2023-05-22T22:00:00"
    }, nullValues = "null")
    void testUpdateEmployee(int id, String name, String job, int salary, String joinDateTimeString) {
        LocalDateTime joinDateTime = LocalDateTime.parse(joinDateTimeString);
        List<EmployeeDto> originalEmployees = getAllEmployees();
        boolean isValidId = originalEmployees
            .stream()
            .anyMatch(employeeDto -> employeeDto.id() == id);
        boolean isValidRequest = isValidId && !StringUtils.isBlank(name) && !StringUtils.isBlank(job)
            && salary > 0 && !joinDateTime.isAfter(LocalDateTime.now());
        EmployeeDto employeeForUpdate = null;

        if (isValidRequest) {
            employeeForUpdate = new EmployeeDto(id, name, job, salary, joinDateTime);
            updateWithValidEmployee(employeeForUpdate);
        } else {
            webTestClient.post().uri(API_EMPLOYEES).exchange().expectStatus().isBadRequest();
        }

        List<EmployeeDto> employeesAfterRequest = getAllEmployees();
        if (isValidRequest) {
            EmployeeDto originalDto = originalEmployees
                .stream().filter(e -> e.id() == id).findFirst().get();
            EmployeeDto updatedDto = employeesAfterRequest
                .stream().filter(e -> e.id() == id).findFirst().get();
            assertEquals(originalDto.id(), updatedDto.id());
            assertThat(employeeForUpdate)
                .usingRecursiveComparison()
                .isEqualTo(updatedDto);
            assertEquals(originalEmployees.size(), employeesAfterRequest.size());
        } else {
            assertThat(employeesAfterRequest)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(originalEmployees);
        }
    }

    private void createValidEmployee(EmployeeDto newEmployee) {
        webTestClient
            .post()
            .uri(API_EMPLOYEES)
            .bodyValue(newEmployee)
            .exchange()
            .expectStatus().isOk();
    }

    private void updateWithValidEmployee(EmployeeDto employeeForUpdate) {
        webTestClient
            .put()
            .uri(API_EMPLOYEES)
            .bodyValue(employeeForUpdate)
            .exchange()
            .expectStatus().isOk();
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
}
