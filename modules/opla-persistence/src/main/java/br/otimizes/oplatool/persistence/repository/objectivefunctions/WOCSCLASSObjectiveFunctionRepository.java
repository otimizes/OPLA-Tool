package br.otimizes.oplatool.persistence.repository.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.WOCSCLASSObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WOCSCLASSObjectiveFunctionRepository extends JpaRepository<WOCSCLASSObjectiveFunction, String> {
}
