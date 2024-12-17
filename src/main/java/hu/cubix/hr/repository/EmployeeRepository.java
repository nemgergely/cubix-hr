package hu.cubix.hr.repository;

import hu.cubix.hr.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByJob(String job);

    List<Employee> findAllByNameStartsWithIgnoreCase(String name);

    List<Employee> findAllByJoinDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Employee> findAllBySalaryGreaterThan(Integer salary);

}
