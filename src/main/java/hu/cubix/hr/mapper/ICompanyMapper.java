package hu.cubix.hr.mapper;

import hu.cubix.hr.dto.CompanyDto;
import hu.cubix.hr.model.Company;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICompanyMapper {

    @Mapping(target = "employees", ignore = true)
    @Named("noEmployees")
    CompanyDto companyToDto(Company company);

    @IterableMapping(qualifiedByName = "noEmployees")
    List<CompanyDto> companiesToDtos(List<Company> companies);

    CompanyDto companyToDtoWithEmployees(Company company);
    List<CompanyDto> companiesToDtosWithEmployees(List<Company> companies);
    Company dtoToCompany(CompanyDto companyDto);
}
