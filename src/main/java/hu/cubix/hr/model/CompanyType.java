package hu.cubix.hr.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CompanyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "companyType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Company> companies;
}
