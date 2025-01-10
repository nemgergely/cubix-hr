package hu.cubix.hr.repository;

import hu.cubix.hr.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {

}
