package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ECObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.ECObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ECObjectiveFunctionService extends BaseService<ECObjectiveFunction> {

    public ECObjectiveFunctionService(ECObjectiveFunctionRepository repository) {
        super(repository);
    }
}
