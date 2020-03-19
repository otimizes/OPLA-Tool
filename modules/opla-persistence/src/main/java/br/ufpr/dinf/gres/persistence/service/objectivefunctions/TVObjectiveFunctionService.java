package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.TVObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class TVObjectiveFunctionService extends BaseService<TVObjectiveFunction> {

    public TVObjectiveFunctionService(TVObjectiveFunctionRepository repository) {
        super(repository);
    }
}
