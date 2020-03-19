package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.AvMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class AvMetricService extends BaseService<TVObjectiveFunction> {

    public AvMetricService(AvMetricRepository repository) {
        super(repository);
    }
}
