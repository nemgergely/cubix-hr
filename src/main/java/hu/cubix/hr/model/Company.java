package hu.cubix.hr.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PositionDetailsByCompany> positionDetailsByCompanyList;

    @ManyToOne
    @JoinColumn(name = "company_type_id", referencedColumnName = "id")
    private CompanyType companyType;

    public void addEmployee(Employee employee) {
        employee.setCompany(this);
        if(this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.getEmployees().add(employee);
    }

    public Company(Integer id, int registrationNumber, String name, String address) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.address = address;
    }
}
