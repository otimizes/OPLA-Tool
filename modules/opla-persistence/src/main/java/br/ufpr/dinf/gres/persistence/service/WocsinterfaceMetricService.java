package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.WOCSINTERFACEObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.WocsinterfaceMetricRepository;
import org.springframework.stereotype.Service;

@Service
public class WocsinterfaceMetricService extends BaseService<WOCSINTERFACEObjectiveFunction> {

    public WocsinterfaceMetricService(WocsinterfaceMetricRepository repository) {
        super(repository);
    }
}
