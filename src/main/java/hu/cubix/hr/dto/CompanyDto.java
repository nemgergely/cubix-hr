package hu.cubix.hr.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;

@JsonFilter("employeeFilter")
public record CompanyDto(
    int id, int registrationNumber, String name, String address, List<EmployeeDto> employees) {
}
