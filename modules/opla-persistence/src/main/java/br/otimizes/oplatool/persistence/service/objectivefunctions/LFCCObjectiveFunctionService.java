package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.LFCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.LFCCObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class LFCCObjectiveFunctionService extends BaseService<LFCCObjectiveFunction> {

    public LFCCObjectiveFunctionService(LFCCObjectiveFunctionRepository repository) {
        super(repository);
    }
}
