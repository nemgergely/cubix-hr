package hu.cubix.hr.repository;

import hu.cubix.hr.model.AverageSalaryByPosition;
import hu.cubix.hr.model.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT DISTINCT c FROM Company c " +
        "JOIN FETCH c.employees e " +
        "WHERE e.salary > :minSalary")
    List<Company> findAllByHavingEmployeeWithSalaryAboveGiven(@Param("minSalary") Integer minSalary);

    @Query(value = "SELECT DISTINCT c FROM Company c " +
        "JOIN FETCH c.employees e " +
        "WHERE COUNT(e) > :employeeLimit")
    List<Company> findAllWithMoreEmployeesThanGiven(@Param("employeeLimit") Integer employeeLimit);

    @Query("SELECT e.position.jobTitle AS position, AVG(e.salary) AS averageSalary "
        + "FROM Company c "
        + "INNER JOIN c.employees e "
        + "WHERE c.id = :companyId "
        + "GROUP BY e.position.jobTitle "
        + "ORDER BY AVG(e.salary) DESC")
    List<AverageSalaryByPosition> findAverageSalariesByPosition(Integer companyId);

    @EntityGraph(attributePaths = {"employees"})
    @Query("SELECT DISTINCT c FROM Company c")
    List<Company> findAllWithEmployees();

    @EntityGraph(attributePaths = {"employees"})
    @Query("SELECT c FROM Company c " +
        "WHERE c.id = :id")
    Optional<Company> findByIdWithEmployees(Integer id);
}
