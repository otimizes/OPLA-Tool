package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.RCCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class RCCObjectiveFunctionService extends BaseService<RCCObjectiveFunction> {

    public RCCObjectiveFunctionService(RCCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
