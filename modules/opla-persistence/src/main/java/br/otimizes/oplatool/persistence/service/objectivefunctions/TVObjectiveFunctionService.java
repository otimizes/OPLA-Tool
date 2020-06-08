package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.TVObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class TVObjectiveFunctionService extends BaseService<TVObjectiveFunction> {

    public TVObjectiveFunctionService(TVObjectiveFunctionRepository repository) {
        super(repository);
    }
}
