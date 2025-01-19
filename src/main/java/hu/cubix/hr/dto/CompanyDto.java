package hu.cubix.hr.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;

@JsonFilter("employeeFilter")
public record CompanyDto(
    Integer id, Integer registrationNumber, String name, String address, List<EmployeeDto> employees) {
}
