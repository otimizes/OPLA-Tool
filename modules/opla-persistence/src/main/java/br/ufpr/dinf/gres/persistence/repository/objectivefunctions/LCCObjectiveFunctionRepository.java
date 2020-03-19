package br.ufpr.dinf.gres.persistence.repository.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.COEObjectiveFunction;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.LCCObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LCCObjectiveFunctionRepository extends JpaRepository<LCCObjectiveFunction, String> {
}
