package br.ufpr.dinf.gres.persistence.service.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ACLASSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.objectivefunctions.ACLASSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ACLASSObjectiveFunctionService extends BaseService<ACLASSObjectiveFunction> {

    public ACLASSObjectiveFunctionService(ACLASSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
