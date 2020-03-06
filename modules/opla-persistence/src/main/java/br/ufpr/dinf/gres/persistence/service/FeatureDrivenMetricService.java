package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.metric.FeatureDrivenMetric;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.FeatureDrivenMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class FeatureDrivenMetricService extends BaseService<FeatureDrivenMetric> {

    public FeatureDrivenMetricService(FeatureDrivenMetricRepository repository) {
        super(repository);
    }
}
