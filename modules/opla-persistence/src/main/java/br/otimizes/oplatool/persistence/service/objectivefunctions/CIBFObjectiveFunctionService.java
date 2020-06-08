package br.otimizes.oplatool.persistence.service.objectivefunctions;

import br.otimizes.oplatool.domain.entity.objectivefunctions.CIBFObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.objectivefunctions.CIBFObjectiveFunctionRepository;
import org.springframework.stereotype.Service;

@Service
public class CIBFObjectiveFunctionService extends BaseService<CIBFObjectiveFunction> {

    public CIBFObjectiveFunctionService(CIBFObjectiveFunctionRepository repository) {
        super(repository);
    }
}
