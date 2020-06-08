package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.DistanceEuclidean;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.DistanceEuclideanRepository;
import org.springframework.stereotype.Service;

@Service
public class DistanceEuclideanService extends BaseService<DistanceEuclidean> {

    public DistanceEuclideanService(DistanceEuclideanRepository repository) {
        super(repository);
    }
}
