package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.CMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CMObjectiveFunctionService extends BaseService<CMObjectiveFunction> {

    public CMObjectiveFunctionService(CMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
