package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.EXTObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.PLAExtensibilityMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class PLAExtensibilityMetricService extends BaseService<EXTObjectiveFunction> {


    public PLAExtensibilityMetricService(PLAExtensibilityMetricRepository repository) {
        super(repository);
    }
}
