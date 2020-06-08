package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.TAMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.TAMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class TAMObjectiveFunctionService extends BaseService<TAMObjectiveFunction> {

    public TAMObjectiveFunctionService(TAMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
