package hu.cubix.hr.controller;

import hu.cubix.hr.dto.EmployeeDto;
import hu.cubix.hr.mapper.IEmployeeMapper;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.service.IEmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final IEmployeeService employeeService;
    private final IEmployeeMapper employeeMapper;

    @GetMapping
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return employeeMapper.employeesToDtos(allEmployees);
    }

    @GetMapping("/job")
    public List<EmployeeDto> findAllEmployeesWithGivenJob(@RequestParam String job) {
        List<Employee> employeesWithGivenJob = employeeService.findAllEmployeesByJob(job);
        return employeeMapper.employeesToDtos(employeesWithGivenJob);
    }

    @GetMapping("/name")
    public List<EmployeeDto> findAllEmployeesWithGivenNamePrefix(@RequestParam String prefix) {
        List<Employee> employeesWithGivenNamePrefix = employeeService.findAllEmployeesWithNamePrefix(prefix);
        return employeeMapper.employeesToDtos(employeesWithGivenNamePrefix);
    }

    @GetMapping("/joinTimeFrame")
    public List<EmployeeDto> findAllEmployeesByJoinTimeFrame(
        @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        List<Employee> employeesWithGivenJoinTimeFrame = employeeService.findAllEmployeesByJoinTimeFrame(from, to);
        return employeeMapper.employeesToDtos(employeesWithGivenJoinTimeFrame);
    }

    @GetMapping("/riches")
    public List<EmployeeDto> findAllEmployeesWithHigherSalary(@RequestParam int salary,
                                                              // page, size, sort paraméterek
                                                              @SortDefault("id") Pageable pageable) {
        Page<Employee> employeePage = employeeService.getRichEmployees(salary, pageable);
        System.out.println(employeePage.getTotalElements());
        System.out.println(employeePage.isLast());
        System.out.println(employeePage.isFirst());
        System.out.println(employeePage.getTotalPages());
        List<Employee> employeesWithHigherSalary = employeePage.getContent();

        return employeeMapper.employeesToDtos(employeesWithHigherSalary);
    }

    @GetMapping("/{id}")
    public EmployeeDto findEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return employeeMapper.employeeToDto(employee);
    }

    @PostMapping
    public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto, BindingResult bindingResult) {
        throwBadRequestExceptionIfAnyErrors(bindingResult);
        Employee employee = employeeMapper.dtoToEmployee(employeeDto);
        Employee newEmployee = employeeService.createEmployee(employee);
        if (newEmployee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return employeeMapper.employeeToDto(newEmployee);
    }

    @PutMapping
    public EmployeeDto updateEmployee(@RequestBody @Valid EmployeeDto employeeDto, BindingResult bindingResult) {
        throwBadRequestExceptionIfAnyErrors(bindingResult);
        employeeDto = new EmployeeDto(
            employeeDto.id(), employeeDto.name(),
            employeeDto.salary(), employeeDto.joinDateTime());
        Employee updatedEmployee = employeeService.updateEmployee(employeeMapper.dtoToEmployee(employeeDto));
        if (updatedEmployee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return employeeMapper.employeeToDto(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployeeById(id);
    }

    @PostMapping("/payRaise")
    public int getPayRaisePercentage(@RequestBody @Valid EmployeeDto employeeDto, BindingResult bindingResult) {
        throwBadRequestExceptionIfAnyErrors(bindingResult);
        Employee employee = employeeMapper.dtoToEmployee(employeeDto);
        return employeeService.getPayRaisePercent(employee);
    }

    private void throwBadRequestExceptionIfAnyErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
