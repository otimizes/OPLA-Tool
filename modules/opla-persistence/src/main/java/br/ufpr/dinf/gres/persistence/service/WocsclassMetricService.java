package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.metric.WocsclassMetric;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.WocsclassMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class WocsclassMetricService extends BaseService<WocsclassMetric> {

    public WocsclassMetricService(WocsclassMetricRepository repository) {
        super(repository);
    }
}
