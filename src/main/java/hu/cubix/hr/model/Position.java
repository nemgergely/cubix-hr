package hu.cubix.hr.model;

import hu.cubix.hr.enums.Qualification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Employee> employees;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PositionDetailsByCompany> positionDetailsByCompanyList;

    public Position(String jobTitle, Qualification qualification) {
        this.jobTitle = jobTitle;
        this.qualification = qualification;
    }
}
