package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.MapObjectiveName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapObjectiveNameRepository extends JpaRepository<MapObjectiveName, String> {
}
