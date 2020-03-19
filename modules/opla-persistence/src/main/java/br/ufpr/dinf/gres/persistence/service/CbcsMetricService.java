package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.CbcsMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class CbcsMetricService extends BaseService<RCCObjectiveFunction> {

    public CbcsMetricService(CbcsMetricRepository repository) {
        super(repository);
    }
}
