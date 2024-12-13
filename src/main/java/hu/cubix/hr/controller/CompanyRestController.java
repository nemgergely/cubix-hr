package hu.cubix.hr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import hu.cubix.hr.dto.CompanyDto;
import hu.cubix.hr.dto.EmployeeDto;
import hu.cubix.hr.mapper.ICompanyMapper;
import hu.cubix.hr.mapper.IEmployeeMapper;
import hu.cubix.hr.model.Company;
import hu.cubix.hr.model.Employee;
import hu.cubix.hr.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/companies")
@AllArgsConstructor
@Slf4j
public class CompanyRestController {

    private final ObjectMapper objectMapper;
    private final CompanyService companyService;
    private final ICompanyMapper companyMapper;
    private final IEmployeeMapper employeeMapper;

    @GetMapping
    public List<CompanyDto> findAllCompanies(@RequestParam Optional<Boolean> full) {
        List<CompanyDto> companyDtos = companyMapper.companiesToDtos(companyService.getAllCompanies());
        boolean filterRequired = full.isEmpty() || full.get().equals(Boolean.FALSE);
        return companyDtos
            .stream()
            .map(company -> setFilterForResponse(filterRequired, company))
            .toList();
    }

    @GetMapping("/{id}")
    public CompanyDto findCompanyById(@PathVariable int id, @RequestParam Optional<Boolean> full) {
        Company company = companyService.getCompanyById(id);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        CompanyDto companyDto = companyMapper.companyToDto(company);
        boolean filterRequired = full.isEmpty() || full.get().equals(Boolean.FALSE);
        return setFilterForResponse(filterRequired, companyDto);
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
        Company newCompany = companyService.createCompany(companyMapper.dtoToCompany(companyDto));
        throwStatusException(newCompany, HttpStatus.BAD_REQUEST);
        return companyMapper.companyToDto(newCompany);
    }

    @PutMapping
    public CompanyDto updateCompany(@RequestBody CompanyDto companyDto) {
        Company updatedCompany = companyService.updateCompany(companyMapper.dtoToCompany(companyDto));
        throwStatusException(updatedCompany, HttpStatus.BAD_REQUEST);
        return companyMapper.companyToDto(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable int id) {
        companyService.deleteCompanyById(id);
    }

    @PostMapping("/{id}/addEmployee")
    public CompanyDto addEmployeeToCompany(
        @PathVariable int id, @RequestBody @Valid EmployeeDto employeeDto, BindingResult bindingResult) {
        throwBadRequestExceptionIfAnyErrors(bindingResult);
        Employee employee = employeeMapper.dtoToEmployee(employeeDto);
        Company companyWithNewEmployee = companyService.addEmployeeToCompany(id, employee);
        throwStatusException(companyWithNewEmployee, HttpStatus.BAD_REQUEST);
        return companyMapper.companyToDto(companyWithNewEmployee);
    }

    @DeleteMapping("/{id}/deleteEmployee/{employeeId}")
    public CompanyDto deleteEmployeeFromCompany(@PathVariable int id, @PathVariable int employeeId) {
        Company companyWithDeletedEmployee = companyService.deleteEmployeeFromCompany(id, employeeId);
        throwStatusException(companyWithDeletedEmployee, HttpStatus.NOT_FOUND);
        return companyMapper.companyToDto(companyWithDeletedEmployee);
    }

    @PutMapping("/{id}/updateEmployees")
    public CompanyDto updateEmployeesOfCompany(
        @PathVariable int id, @RequestBody List<@Valid EmployeeDto> employeeDtos, BindingResult bindingResult) {
        throwBadRequestExceptionIfAnyErrors(bindingResult);
        List<Employee> newEmployees = employeeMapper.dtosToEmployees(employeeDtos);
        Company companyWithUpdatedEmployees = companyService.updateEmployeesOfCompany(id, newEmployees);
        throwStatusException(companyWithUpdatedEmployees, HttpStatus.BAD_REQUEST);
        return companyMapper.companyToDto(companyWithUpdatedEmployees);
    }

    private void throwStatusException(Company company, HttpStatus status) {
        if (company == null) {
            throw new ResponseStatusException(status);
        }
    }

    private void throwBadRequestExceptionIfAnyErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private CompanyDto setFilterForResponse(boolean filterRequired, CompanyDto companyDto) {
        try {
            ObjectWriter writer;
            FilterProvider filter;
            if (filterRequired) {
                filter = new SimpleFilterProvider()
                    .setFailOnUnknownId(true)
                    .addFilter("employeeFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept("employees"));
            } else {
                filter = new SimpleFilterProvider()
                    .setFailOnUnknownId(true)
                    .addFilter("employeeFilter",
                        SimpleBeanPropertyFilter.serializeAll());
            }
            objectMapper.setFilterProvider(filter);
            writer = objectMapper.writer(filter);
            String writeValueAsString = writer.writeValueAsString(companyDto);
            return objectMapper.readValue(writeValueAsString, CompanyDto.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
