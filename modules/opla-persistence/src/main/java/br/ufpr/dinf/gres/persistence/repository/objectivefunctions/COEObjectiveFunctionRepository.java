package br.ufpr.dinf.gres.persistence.repository.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ACOMPObjectiveFunction;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.COEObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface COEObjectiveFunctionRepository extends JpaRepository<COEObjectiveFunction, String> {
}
