package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.DCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.DCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class DCObjectiveFunctionService extends BaseService<DCObjectiveFunction> {

    public DCObjectiveFunctionService(DCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
