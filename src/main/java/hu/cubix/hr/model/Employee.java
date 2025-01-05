package hu.cubix.hr.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer salary;

    @Column(name = "join_date_time")
    private LocalDateTime joinDateTime;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    public Employee(String name, Integer salary, LocalDateTime joinDateTime, Position position) {
        this.name = name;
        this.salary = salary;
        this.joinDateTime = joinDateTime;
        this.position = position;
    }

    public Employee(Double averageSalary, Position position) {
        this.salary = averageSalary.intValue();
        this.position = position;
    }
}
