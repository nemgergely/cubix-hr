package hu.cubix.hr.repository;

import hu.cubix.hr.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    List<Employee> findByPositionJobTitle(String job);

    List<Employee> findByNameStartingWithIgnoreCase(String prefix);

    List<Employee> findByJoinDateTimeBetween(LocalDateTime from, LocalDateTime to);

    Page<Employee> findBySalaryGreaterThan(Integer salary, Pageable pageable);

    @Modifying
    @Query("UPDATE Employee e "
        + "SET e.salary = :minSalary "
        + "WHERE e.company.id = :companyId "
        + "AND e.position.jobTitle = :jobTitle "
        + "AND e.salary < :minSalary")
    void updateSalaries(Integer companyId, String jobTitle, int minSalary);

    void deleteByCompanyId(int companyId);
}
