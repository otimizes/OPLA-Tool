package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.ExperimentConfiguration;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.ExperimentConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperimentConfigurationService extends BaseService<ExperimentConfiguration> {

    public ExperimentConfigurationService(ExperimentConfigurationRepository repository) {
        super(repository);
    }
}
