package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ACLASSObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.ACLASSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ACLASSObjectiveFunctionService extends BaseService<ACLASSObjectiveFunction> {

    public ACLASSObjectiveFunctionService(ACLASSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
