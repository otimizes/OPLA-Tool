package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.FeatureDrivenMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class FeatureDrivenMetricService extends BaseService<FMObjectiveFunction> {

    public FeatureDrivenMetricService(FeatureDrivenMetricRepository repository) {
        super(repository);
    }
}
