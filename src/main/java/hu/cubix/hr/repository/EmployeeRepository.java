package hu.cubix.hr.repository;

import hu.cubix.hr.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByJob(String job);

    List<Employee> findByNameStartingWithIgnoreCase(String prefix);

    List<Employee> findByJoinDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Employee> findBySalaryGreaterThan(Integer salary);

}
