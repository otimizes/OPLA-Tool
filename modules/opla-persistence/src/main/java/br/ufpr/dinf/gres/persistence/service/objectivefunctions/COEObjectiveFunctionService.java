package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.COEObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.COEObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class COEObjectiveFunctionService extends BaseService<COEObjectiveFunction> {

    public COEObjectiveFunctionService(COEObjectiveFunctionRepository repository) {
        super(repository);
    }
}
