package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.Info;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.InfoRepository;
import org.springframework.stereotype.Service;

@Service
public class InfoService extends BaseService<Info> {

    public InfoService(InfoRepository repository) {
        super(repository);
    }
}
