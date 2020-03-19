package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TAMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.TAMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class TAMObjectiveFunctionService extends BaseService<TAMObjectiveFunction> {

    public TAMObjectiveFunctionService(TAMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
