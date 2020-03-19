package br.ufpr.dinf.gres.persistence.repository.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.DCObjectiveFunction;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ECObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECObjectiveFunctionRepository extends JpaRepository<ECObjectiveFunction, String> {
}
