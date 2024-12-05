package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeTLController {

    private final List<EmployeeDto> employees = new ArrayList<>();

    @GetMapping("/")
    public String listEmployees(Map<String, Object> model) {
        model.put("employees", employees);
        model.put("newEmployee", new EmployeeDto());
        return "index";
    }

    @PostMapping("/employee")
    public String createEmployee(EmployeeDto employee) {
        employees.add(employee);
        return "redirect:/";
    }
}
