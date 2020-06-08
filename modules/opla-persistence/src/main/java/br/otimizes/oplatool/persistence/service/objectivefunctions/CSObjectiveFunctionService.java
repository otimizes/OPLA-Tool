package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.CSObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.WOCSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CSObjectiveFunctionService extends BaseService<CSObjectiveFunction> {

    public CSObjectiveFunctionService(WOCSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
