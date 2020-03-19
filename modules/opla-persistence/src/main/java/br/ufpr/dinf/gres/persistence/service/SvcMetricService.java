package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.SvcMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class SvcMetricService extends BaseService<SVObjectiveFunction> {

    public SvcMetricService(SvcMetricRepository repository) {
        super(repository);
    }
}
