package hu.cubix.hr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private Integer id;
    private String name;
    private String job;
    private Integer salary;
    private LocalDateTime joinDateTime;
}
