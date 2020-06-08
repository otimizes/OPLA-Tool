package br.otimizes.oplatool.persistence.repository.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ACLASSObjectiveFunction;
import br.otimizes.oplatool.domain.entity.objectivefunctions.CIBFObjectiveFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CIBFObjectiveFunctionRepository extends JpaRepository<CIBFObjectiveFunction, String> {
}
