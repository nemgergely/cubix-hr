package hu.cubix.hr.repository;

import hu.cubix.hr.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query(value = "SELECT c FROM Company c " +
        "INNER JOIN Employee e ON e.company.id = c.id " +
        "WHERE e.salary > :salaryLimit")
    List<Company> findAllByHavingEmployeeWithSalaryAboveGiven(@Param("salaryLimit") Integer salaryLimit);

    @Query(value = "SELECT c FROM Company c " +
        "WHERE (SELECT COUNT(e) FROM Employee e " +
        "WHERE e.company.id = c.id) > :employeeLimit")
    List<Company> findAllWithMoreEmployeesThanGiven(@Param("employeeLimit") Integer employeeLimit);
}
