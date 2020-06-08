package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.WOCSCLASSObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.WOCSCLASSObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class WOCSCLASSObjectiveFunctionService extends BaseService<WOCSCLASSObjectiveFunction> {

    public WOCSCLASSObjectiveFunctionService(WOCSCLASSObjectiveFunctionRepository repository) {
        super(repository);
    }
}
