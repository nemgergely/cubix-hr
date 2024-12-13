package hu.cubix.hr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EmployeeDto(
    int id, @NotEmpty String name, @NotEmpty String job, @Positive int salary, @Past LocalDateTime joinDateTime) {

    public EmployeeDto() {
        this( 0, null, null, 1, null);
    }
}
