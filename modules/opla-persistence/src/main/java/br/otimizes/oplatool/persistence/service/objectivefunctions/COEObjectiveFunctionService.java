package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.COEObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.COEObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class COEObjectiveFunctionService extends BaseService<COEObjectiveFunction> {

    public COEObjectiveFunctionService(COEObjectiveFunctionRepository repository) {
        super(repository);
    }
}
