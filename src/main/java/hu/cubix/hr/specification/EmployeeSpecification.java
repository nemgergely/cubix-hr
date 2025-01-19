package hu.cubix.hr.specification;

import hu.cubix.hr.model.Employee;
import hu.cubix.hr.model.Employee_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class EmployeeSpecification {

    public static Specification<Employee> idMatches(Integer id) {
        return (root, cq, cb) -> cb.equal(root.get(Employee_.id), id);
    }

    public static Specification<Employee> nameStartsWith(String prefix) {
        return (root, cq, cb) ->
            cb.like(cb.lower(root.get(Employee_.name)), prefix.toLowerCase() + "%");
    }

    public static Specification<Employee> jobTitleMatches(String jobTitle) {
        return (root, cq, cb) ->
            cb.equal(root.get(Employee_.position.getName()), jobTitle);
    }

    public static Specification<Employee> salaryWithinFivePercentMargin(Integer salary) {
        return (root, cq, cb) ->
            cb.between(root.get(Employee_.salary), salary / 100 * 95, salary / 100 * 105);
    }

    public static Specification<Employee> joinDateMatches(LocalDateTime joinDateTime) {
        return (root, cq, cb) ->
            cb.between(root.get(Employee_.joinDateTime),
                joinDateTime.with(LocalTime.MIN), joinDateTime.with(LocalTime.MAX));
    }

    public static Specification<Employee> companyNameStartsWith(String prefix) {
        return (root, cq, cb) ->
            cb.like(cb.lower(root.get(Employee_.company.getName())), prefix.toLowerCase() + "%");
    }
}
