package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.ExecutionRepository;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService extends BaseService<Execution> {

    public ExecutionService(ExecutionRepository repository) {
        super(repository);
    }
}
