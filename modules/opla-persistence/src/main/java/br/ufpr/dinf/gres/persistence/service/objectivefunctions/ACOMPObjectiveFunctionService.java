package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ACOMPObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.ACOMPObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ACOMPObjectiveFunctionService extends BaseService<ACOMPObjectiveFunction> {

    public ACOMPObjectiveFunctionService(ACOMPObjectiveFunctionRepository repository) {
        super(repository);
    }
}
