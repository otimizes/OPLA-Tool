package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.FMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class FMObjectiveFunctionService extends BaseService<FMObjectiveFunction> {

    public FMObjectiveFunctionService(FMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
