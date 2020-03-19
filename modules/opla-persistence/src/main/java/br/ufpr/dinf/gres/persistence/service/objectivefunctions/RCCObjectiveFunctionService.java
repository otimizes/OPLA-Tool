package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.RCCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class RCCObjectiveFunctionService extends BaseService<RCCObjectiveFunction> {

    public RCCObjectiveFunctionService(RCCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
