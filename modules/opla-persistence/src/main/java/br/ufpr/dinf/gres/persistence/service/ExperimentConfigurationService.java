package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.opla.entity.ExperimentConfiguration;
import br.ufpr.dinf.gres.persistence.repository.ExperimentConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperimentConfigurationService extends BaseService<ExperimentConfiguration>{

    public ExperimentConfigurationService(ExperimentConfigurationRepository repository) {
        super(repository);
    }
}
