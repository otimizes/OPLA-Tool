package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.SVObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class SVObjectiveFunctionService extends BaseService<SVObjectiveFunction> {

    public SVObjectiveFunctionService(SVObjectiveFunctionRepository repository) {
        super(repository);
    }
}
