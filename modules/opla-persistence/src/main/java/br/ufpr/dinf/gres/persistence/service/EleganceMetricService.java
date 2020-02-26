package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.EleganceMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class EleganceMetricService extends BaseService<EleganceMetric> {

    public EleganceMetricService(EleganceMetricRepository repository) {
        super(repository);
    }
}
