package hu.cubix.hr.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {

    private int id;
    private String name;
    private String job;
    private int salary;
    private LocalDateTime joinDateTime;
}
