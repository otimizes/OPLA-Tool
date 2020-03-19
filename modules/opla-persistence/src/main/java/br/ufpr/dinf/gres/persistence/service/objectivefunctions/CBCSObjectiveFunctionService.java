package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CBCSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.CBCSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CBCSObjectiveFunctionService extends BaseService<CBCSObjectiveFunction> {

    public CBCSObjectiveFunctionService(CBCSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
