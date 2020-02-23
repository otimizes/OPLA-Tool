package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.opla.entity.metric.AvMetric;
import br.ufpr.dinf.gres.persistence.repository.AvMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class AvMetricService extends BaseService<AvMetric> {

    public AvMetricService(AvMetricRepository repository) {
        super(repository);
    }
}
