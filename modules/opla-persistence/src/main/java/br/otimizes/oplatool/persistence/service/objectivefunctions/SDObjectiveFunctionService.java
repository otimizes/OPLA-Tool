package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.SDObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.SDObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class SDObjectiveFunctionService extends BaseService<SDObjectiveFunction> {


    public SDObjectiveFunctionService(SDObjectiveFunctionRepository repository) {
        super(repository);
    }
}
