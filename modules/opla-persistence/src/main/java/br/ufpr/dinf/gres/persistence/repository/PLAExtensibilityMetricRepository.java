package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.EXTObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PLAExtensibilityMetricRepository extends JpaRepository<EXTObjectiveFunction, String> {
}
