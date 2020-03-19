package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.ELEGObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ELEGObjectiveFunctionService extends BaseService<ELEGObjectiveFunction> {

    public ELEGObjectiveFunctionService(ELEGObjectiveFunctionRepository repository) {
        super(repository);
    }
}
