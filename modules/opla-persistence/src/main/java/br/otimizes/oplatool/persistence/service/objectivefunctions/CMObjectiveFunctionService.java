package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.CMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.CMObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CMObjectiveFunctionService extends BaseService<CMObjectiveFunction> {

    public CMObjectiveFunctionService(CMObjectiveFunctionRepository repository) {
        super(repository);
    }
}
