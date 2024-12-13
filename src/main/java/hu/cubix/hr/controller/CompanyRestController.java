package hu.cubix.hr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import hu.cubix.hr.dto.CompanyDto;
import hu.cubix.hr.dto.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/companies")
@AllArgsConstructor
@Slf4j
public class CompanyRestController {

    private final ObjectMapper objectMapper;
    private final Map<Integer, CompanyDto> companies = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<CompanyDto>> findAllCompanies(@RequestParam Optional<Boolean> full) {
        List<CompanyDto> companyList = new ArrayList<>(companies.values());
        boolean filterRequired = full.isEmpty() || full.get().equals(Boolean.FALSE);
        return ResponseEntity.ok(companyList
            .stream()
            .map(company -> setFilterForResponse(filterRequired, company))
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> findCompanyById(@PathVariable int id, @RequestParam Optional<Boolean> full) {
        if (!companies.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        CompanyDto company = companies.get(id);
        boolean filterRequired = full.isEmpty() || full.get().equals(Boolean.FALSE);
        return ResponseEntity.ok(setFilterForResponse(filterRequired, company));
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto company) {
        if (companies.containsKey(company.getId())) {
            return ResponseEntity.badRequest().build();
        }
        companies.put(company.getId(), company);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable int id, @RequestBody CompanyDto company) {
        company.setId(id);
        if (!companies.containsKey(company.getId())) {
            return ResponseEntity.notFound().build();
        }
        companies.put(id, company);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable int id) {
        companies.remove(id);
    }

    @PostMapping("/{id}/addEmployee")
    public ResponseEntity<CompanyDto> addEmployeeToCompany(@PathVariable int id, @RequestBody EmployeeDto employee) {
        if (!companies.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        CompanyDto company = companies.get(id);
        company.getEmployees().add(employee);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}/deleteEmployee/{employeeId}")
    public ResponseEntity<CompanyDto> deleteEmployeeFromCompany(@PathVariable int id, @PathVariable int employeeId) {
        if (!companies.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        CompanyDto company = companies.get(id);
        company.getEmployees()
            .stream()
            .filter(employeeDto -> employeeDto.getId() == employeeId)
            .findFirst()
            .ifPresent(e -> company.getEmployees().remove(e));
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}/updateEmployees")
    public ResponseEntity<CompanyDto> updateEmployeesOfCompany(
        @PathVariable int id, @RequestBody List<EmployeeDto> employees) {
        if (!companies.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        CompanyDto company = companies.get(id);
        company.setEmployees(employees);
        return ResponseEntity.ok(company);
    }

    private CompanyDto setFilterForResponse(boolean filterRequired, CompanyDto company) {
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
            String writeValueAsString = writer.writeValueAsString(company);
            return objectMapper.readValue(writeValueAsString, CompanyDto.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
