package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.LCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.LCCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class LCCObjectiveFunctionService extends BaseService<LCCObjectiveFunction> {

    public LCCObjectiveFunctionService(LCCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
