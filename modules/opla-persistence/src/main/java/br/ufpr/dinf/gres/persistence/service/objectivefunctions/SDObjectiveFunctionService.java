package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SDObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.SDObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class SDObjectiveFunctionService extends BaseService<SDObjectiveFunction> {


    public SDObjectiveFunctionService(SDObjectiveFunctionRepository repository) {
        super(repository);
    }
}
