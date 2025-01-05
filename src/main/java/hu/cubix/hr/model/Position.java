package hu.cubix.hr.model;

import hu.cubix.hr.enums.Qualification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Column(name = "min_salary")
    private Integer minSalary;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
}
