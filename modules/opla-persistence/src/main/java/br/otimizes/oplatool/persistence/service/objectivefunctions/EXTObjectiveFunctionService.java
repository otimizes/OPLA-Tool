package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.EXTObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.EXTObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class EXTObjectiveFunctionService extends BaseService<EXTObjectiveFunction> {


    public EXTObjectiveFunctionService(EXTObjectiveFunctionRepository repository) {
        super(repository);
    }
}
