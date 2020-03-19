package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ECObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.ECObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ECObjectiveFunctionService extends BaseService<ECObjectiveFunction> {

    public ECObjectiveFunctionService(ECObjectiveFunctionRepository repository) {
        super(repository);
    }
}
