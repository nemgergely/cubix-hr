package hu.cubix.hr.repository;

import hu.cubix.hr.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByJob(String job);

    List<Employee> findByNameStartingWithIgnoreCase(String prefix);

    List<Employee> findByJoinDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Employee> findBySalaryGreaterThan(Integer salary);

    @Query(value = "SELECT NEW hu.cubix.hr.model.Employee(AVG(e.salary), e.position) FROM Employee e " +
        "WHERE e.position.company.id = :companyId " +
        "GROUP BY e.position.name " +
        "ORDER BY AVG(e.salary) DESC")
    List<Employee> findAverageSalariesOfGivenCompanyIdGroupedByJobOrderByAverageSalaries(Integer companyId);
}
