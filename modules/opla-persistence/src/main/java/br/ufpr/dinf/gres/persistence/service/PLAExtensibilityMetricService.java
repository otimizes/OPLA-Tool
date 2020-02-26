package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.persistence.repository.PLAExtensibilityMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class PLAExtensibilityMetricService extends BaseService<PLAExtensibilityMetric>{


    public PLAExtensibilityMetricService(PLAExtensibilityMetricRepository repository) {
        super(repository);
    }
}
