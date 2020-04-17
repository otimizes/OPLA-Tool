package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.WOCSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CSObjectiveFunctionService extends BaseService<CSObjectiveFunction> {

    public CSObjectiveFunctionService(WOCSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
