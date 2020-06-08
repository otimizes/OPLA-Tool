package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.DCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.DCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class DCObjectiveFunctionService extends BaseService<DCObjectiveFunction> {

    public DCObjectiveFunctionService(DCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
