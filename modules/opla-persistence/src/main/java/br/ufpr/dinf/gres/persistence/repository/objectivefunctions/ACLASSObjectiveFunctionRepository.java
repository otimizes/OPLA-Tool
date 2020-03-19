package br.ufpr.dinf.gres.persistence.repository.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ACLASSObjectiveFunction;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ACLASSObjectiveFunctionRepository extends JpaRepository<ACLASSObjectiveFunction, String> {
}
