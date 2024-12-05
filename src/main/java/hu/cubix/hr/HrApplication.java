package hu.cubix.hr;

import hu.cubix.hr.model.Employee;
import hu.cubix.hr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Employee employee1 = new Employee(1, "Vasutas", 1000,
			LocalDateTime.of(2010, 5, 10, 10, 0, 0));
		Employee employee2 = new Employee(1, "Pályamunkás", 1000,
			LocalDateTime.of(2016, 5, 10, 10, 0, 0));
		Employee employee3 = new Employee(1, "Vonatkerék pumpáló", 1000,
			LocalDateTime.of(2021, 5, 10, 10, 0, 0));
		Employee employee4 = new Employee(1, "Lakatlan szigeten tömeggyilkos", 1000,
			LocalDateTime.of(2023, 5, 10, 10, 0, 0));

		String format = "Base salary %d was increased by %d percent. New value is: %d%n";

		System.out.format(format,
			employee1.getSalary(), salaryService.getEmployeeService().getPayRaisePercent(employee1),
			salaryService.setNewSalary(employee1));
		System.out.format(format,
			employee2.getSalary(), salaryService.getEmployeeService().getPayRaisePercent(employee2),
			salaryService.setNewSalary(employee2));
		System.out.format(format,
			employee3.getSalary(), salaryService.getEmployeeService().getPayRaisePercent(employee3),
			salaryService.setNewSalary(employee3));
		System.out.format(format,
			employee4.getSalary(), salaryService.getEmployeeService().getPayRaisePercent(employee4),
			salaryService.setNewSalary(employee4));
	}
}
