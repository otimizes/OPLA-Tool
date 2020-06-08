package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.FMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class FMObjectiveFunctionService extends BaseService<FMObjectiveFunction> {

    public FMObjectiveFunctionService(FMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
