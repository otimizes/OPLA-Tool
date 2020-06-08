package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.ExperimentRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperimentService extends BaseService<Experiment> {

    public ExperimentService(ExperimentRepository repository) {
        super(repository);
    }
}
