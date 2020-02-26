package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.persistence.repository.ExecutionRepository;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService extends BaseService<Execution>{

    public ExecutionService(ExecutionRepository repository) {
        super(repository);
    }
}
