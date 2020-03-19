package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CMObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConventionalMetricRepository extends JpaRepository<CMObjectiveFunction, String> {
}
