package hu.cubix.hr.repository;

import hu.cubix.hr.model.PositionDetailsByCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionDetailsByCompanyRepository extends JpaRepository<PositionDetailsByCompany, Integer> {

    List<PositionDetailsByCompany> findByPositionJobTitleAndCompanyId(String jobTitle, Integer companyId);
}
