package hu.cubix.hr.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {

    private Integer id;
    private String name;
    private String job;
    private Integer salary;
    private LocalDateTime joinDateTime;
}
