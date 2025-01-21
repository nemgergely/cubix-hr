package hu.cubix.hr.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    public Employee(String name, Integer salary, LocalDateTime joinDateTime) {
        this.name = name;
        this.salary = salary;
        this.joinDateTime = joinDateTime;
    }
}
