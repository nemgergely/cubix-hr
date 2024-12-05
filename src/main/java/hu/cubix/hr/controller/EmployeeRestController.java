package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final Map<Integer, EmployeeDto> employees = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAllEmployees() {
        return ResponseEntity.ok(new ArrayList<>(employees.values()));
    }

    @GetMapping("/riches")
    public ResponseEntity<List<EmployeeDto>> findAllEmployeesWithHigherSalary(@RequestParam int salary) {
        List<EmployeeDto> employeesWithHigherSalary = employees.values()
            .stream()
            .filter( employee -> employee.getSalary() > salary)
            .toList();
        return ResponseEntity.ok(employeesWithHigherSalary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findEmployeeById(@PathVariable int id) {
        EmployeeDto employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employee) {
        if (employees.containsKey(employee.getId())) {
            return ResponseEntity.badRequest().build();
        }
        employees.put(employee.getId(), employee);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable int id, @RequestBody EmployeeDto employee) {
        employee.setId(id);
        if (!employees.containsKey(employee.getId())) {
            return ResponseEntity.notFound().build();
        }
        employees.put(id, employee);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employees.remove(id);
    }
}
