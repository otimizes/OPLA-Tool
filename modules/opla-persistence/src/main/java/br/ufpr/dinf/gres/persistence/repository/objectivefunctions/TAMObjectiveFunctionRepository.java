package br.ufpr.dinf.gres.persistence.repository.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.LCCObjectiveFunction;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TAMObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TAMObjectiveFunctionRepository extends JpaRepository<TAMObjectiveFunction, String> {
}
