package hu.cubix.hr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private Integer id;
    private int registrationNumber;
    private String name;
    private String address;
    private List<Employee> employees;
}
