package hu.cubix.hr.mapper;

import hu.cubix.hr.dto.EmployeeDto;
import hu.cubix.hr.model.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IEmployeeMapper {

    EmployeeDto employeeToDto(Employee employee);
    List<EmployeeDto> employeesToDtos(List<Employee> employees);
    Employee dtoToEmployee(EmployeeDto employeeDto);
    List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
