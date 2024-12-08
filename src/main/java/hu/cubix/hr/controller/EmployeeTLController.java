package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeTLController {

    private static final String MAIN_PAGE_REDIRECT = "redirect:/";
    private final List<EmployeeDto> employees = new ArrayList<>();

    @GetMapping("/")
    public String listEmployees(Model model) {
        model.addAllAttributes(Map.of("employees", employees));
        model.addAttribute("newEmployee", new EmployeeDto());
        return "index";
    }

    @GetMapping("/update/{id}")
    public String getUpdateEmployeeForm(@PathVariable int id, Model model) {
        employees.stream().filter(e -> e.getId() == id).findFirst()
            .ifPresent(e -> model.addAttribute("modifiedEmployee", e));
        return "update-employee";
    }

    @PostMapping("/create")
    public String createEmployee(EmployeeDto newEmployee) {
        if (employees.stream().noneMatch(employee -> employee.getId().equals(newEmployee.getId()))) {
            employees.add(newEmployee);
        }
        return MAIN_PAGE_REDIRECT;
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, EmployeeDto modifiedEmployee) {
        employees.removeIf(employee -> employee.getId().equals(id));
        employees.add(modifiedEmployee);
        return MAIN_PAGE_REDIRECT;
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        employees.removeIf(employee -> employee.getId().equals(id));
        return MAIN_PAGE_REDIRECT;
    }
}
