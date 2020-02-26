package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.persistence.repository.ExperimentRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperimentService extends BaseService<Experiment> {

    public ExperimentService(ExperimentRepository repository) {
        super(repository);
    }
}
