package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.Objective;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.ObjectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveService extends BaseService<Objective> {

    public ObjectiveService(ObjectiveRepository repository) {
        super(repository);
    }
}
