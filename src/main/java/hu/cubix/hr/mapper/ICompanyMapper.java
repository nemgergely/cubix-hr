package hu.cubix.hr.mapper;

import hu.cubix.hr.dto.CompanyDto;
import hu.cubix.hr.model.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICompanyMapper {

    CompanyDto companyToDto(Company company);
    List<CompanyDto> companiesToDtos(List<Company> companies);
    Company dtoToCompany(CompanyDto companyDto);
}
