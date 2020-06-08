package br.otimizes.oplatool.persistence.repository.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ACOMPObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ACOMPObjectiveFunctionRepository extends JpaRepository<ACOMPObjectiveFunction, String> {
}
