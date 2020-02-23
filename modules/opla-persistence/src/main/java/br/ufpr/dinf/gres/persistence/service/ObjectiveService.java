package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.opla.entity.Objective;
import br.ufpr.dinf.gres.persistence.repository.ObjectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveService extends BaseService<Objective>{

    public ObjectiveService(ObjectiveRepository repository) {
        super(repository);
    }
}
