package hu.cubix.hr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Employee {

    private Integer id;
    private String job;
    private Integer salary;
    private LocalDateTime joinDateTime;
}
