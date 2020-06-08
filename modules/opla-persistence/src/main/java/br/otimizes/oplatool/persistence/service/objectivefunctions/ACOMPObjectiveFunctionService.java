package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ACOMPObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.ACOMPObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ACOMPObjectiveFunctionService extends BaseService<ACOMPObjectiveFunction> {

    public ACOMPObjectiveFunctionService(ACOMPObjectiveFunctionRepository repository) {
        super(repository);
    }
}
