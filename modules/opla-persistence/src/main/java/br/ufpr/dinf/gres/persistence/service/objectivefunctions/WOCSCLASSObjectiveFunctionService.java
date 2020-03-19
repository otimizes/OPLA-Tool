package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.WOCSCLASSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.WOCSCLASSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class WOCSCLASSObjectiveFunctionService extends BaseService<WOCSCLASSObjectiveFunction> {

    public WOCSCLASSObjectiveFunctionService(WOCSCLASSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
