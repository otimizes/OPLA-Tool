package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.WOCSINTERFACEObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.WOCSINTERFACEObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class WOCSINTERFACEObjectiveFunctionService extends BaseService<WOCSINTERFACEObjectiveFunction> {

    public WOCSINTERFACEObjectiveFunctionService(WOCSINTERFACEObjectiveFunctionRepository repository) {
        super(repository);
    }
}
