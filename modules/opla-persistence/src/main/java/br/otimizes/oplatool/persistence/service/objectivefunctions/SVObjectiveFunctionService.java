package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.SVObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.SVObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class SVObjectiveFunctionService extends BaseService<SVObjectiveFunction> {

    public SVObjectiveFunctionService(SVObjectiveFunctionRepository repository) {
        super(repository);
    }
}
