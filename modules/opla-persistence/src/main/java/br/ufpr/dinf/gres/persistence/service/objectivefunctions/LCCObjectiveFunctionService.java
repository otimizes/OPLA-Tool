package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.LCCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.LCCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class LCCObjectiveFunctionService extends BaseService<LCCObjectiveFunction> {

    public LCCObjectiveFunctionService(LCCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
