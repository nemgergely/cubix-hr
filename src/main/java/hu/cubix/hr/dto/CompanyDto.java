package hu.cubix.hr.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonFilter("employeeFilter")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto implements Serializable {

    private int id;
    private int registrationNumber;
    private String name;
    private String address;
    private List<EmployeeDto> employees;
}
