package hu.cubix.hr;

import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.model.Position;
import hu.cubix.hr.service.InitDbService;
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

	@Autowired
	InitDbService initDbService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Employee employee1 = new Employee("Vasutas János", 1000,
			LocalDateTime.of(2010, 5, 10, 10, 0, 0), new Position());
		Employee employee2 = new Employee("Pályamunkás Pál", 1000,
			LocalDateTime.of(2016, 5, 10, 10, 0, 0), new Position());
		Employee employee3 = new Employee("Vonatkerék Vince", 1000,
			LocalDateTime.of(2021, 5, 10, 10, 0, 0), new Position());
		Employee employee4 = new Employee("Lakatlan László", 1000,
			LocalDateTime.of(2023, 5, 10, 10, 0, 0), new Position());

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

		initDbService.clearDb();
		initDbService.insertTestData();
	}
}
