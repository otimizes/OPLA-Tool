package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.EXTObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.EXTObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class EXTObjectiveFunctionService extends BaseService<EXTObjectiveFunction> {


    public EXTObjectiveFunctionService(EXTObjectiveFunctionRepository repository) {
        super(repository);
    }
}
