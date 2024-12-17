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
    private String job;
    private Integer salary;

    @Column(name = "join_date_time")
    private LocalDateTime joinDateTime;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    public Employee(String name, String job, Integer salary, LocalDateTime joinDateTime, Company company) {
        this.name = name;
        this.job = job;
        this.salary = salary;
        this.joinDateTime = joinDateTime;
        this.company = company;
    }
}
