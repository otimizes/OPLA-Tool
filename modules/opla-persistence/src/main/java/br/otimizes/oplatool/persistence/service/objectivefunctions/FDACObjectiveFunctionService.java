package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.FDACObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.FDACObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class FDACObjectiveFunctionService extends BaseService<FDACObjectiveFunction> {

    public FDACObjectiveFunctionService(FDACObjectiveFunctionRepository repository) {
        super(repository);
    }
}
