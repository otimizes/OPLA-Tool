package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.Objective;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.ObjectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveService extends BaseService<Objective> {

    public ObjectiveService(ObjectiveRepository repository) {
        super(repository);
    }
}
