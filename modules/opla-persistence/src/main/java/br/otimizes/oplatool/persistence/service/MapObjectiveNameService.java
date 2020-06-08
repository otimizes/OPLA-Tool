package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.MapObjectiveName;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.MapObjectiveNameRepository;
import org.springframework.stereotype.Service;

@Service
public class MapObjectiveNameService extends BaseService<MapObjectiveName> {

    public MapObjectiveNameService(MapObjectiveNameRepository repository) {
        super(repository);
    }
}
