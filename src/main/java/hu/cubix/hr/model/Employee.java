package hu.cubix.hr.model;

import java.time.LocalDateTime;

public class Employee {

    private Integer id;
    private String job;
    private Integer salary;
    private LocalDateTime joinDateTime;

    public Employee(Integer id, String job, Integer salary, LocalDateTime joinDateTime) {
        this.id = id;
        this.job = job;
        this.salary = salary;
        this.joinDateTime = joinDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public LocalDateTime getJoinDateTime() {
        return joinDateTime;
    }

    public void setJoinDateTime(LocalDateTime joinDateTime) {
        this.joinDateTime = joinDateTime;
    }
}
