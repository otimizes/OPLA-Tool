package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;
import br.ufpr.dinf.gres.persistence.repository.ConventionalMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class ConventionalMetricService extends BaseService<ConventionalMetric> {

    public ConventionalMetricService(ConventionalMetricRepository repository) {
        super(repository);
    }
}
