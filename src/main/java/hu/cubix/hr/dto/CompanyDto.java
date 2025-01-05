package hu.cubix.hr.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import hu.cubix.hr.model.Position;

import java.util.List;

@JsonFilter("positionFilter")
public record CompanyDto(
    Integer id, int registrationNumber, String name, String address, List<Position> positions) {
}
