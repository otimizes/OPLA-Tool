package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.ELEGObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class ELEGObjectiveFunctionService extends BaseService<ELEGObjectiveFunction> {

    public ELEGObjectiveFunctionService(ELEGObjectiveFunctionRepository repository) {
        super(repository);
    }
}
