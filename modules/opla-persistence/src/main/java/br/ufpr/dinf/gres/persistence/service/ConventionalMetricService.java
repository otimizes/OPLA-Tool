package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.ConventionalMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class ConventionalMetricService extends BaseService<CMObjectiveFunction> {

    public ConventionalMetricService(ConventionalMetricRepository repository) {
        super(repository);
    }
}
