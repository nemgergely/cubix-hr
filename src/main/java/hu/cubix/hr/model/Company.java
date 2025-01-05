package hu.cubix.hr.model;

import hu.cubix.hr.enums.CompanyForm;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "registration_number")
    private int registrationNumber;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private CompanyForm companyForm;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private List<Position> positions;

    public Company(int registrationNumber, String name, String address, CompanyForm companyForm) {
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.address = address;
        this.companyForm = companyForm;
    }
}
