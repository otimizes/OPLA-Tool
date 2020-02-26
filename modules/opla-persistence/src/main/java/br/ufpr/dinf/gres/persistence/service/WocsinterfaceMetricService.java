package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.metric.WocsinterfaceMetric;
import br.ufpr.dinf.gres.persistence.repository.WocsinterfaceMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class WocsinterfaceMetricService extends BaseService<WocsinterfaceMetric>{

    public WocsinterfaceMetricService(WocsinterfaceMetricRepository repository) {
        super(repository);
    }
}
