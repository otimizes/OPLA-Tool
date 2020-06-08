package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.MapObjectiveName;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map-objective-name")
public class MapObjectiveNameResource extends BaseResource<MapObjectiveName> {

    public MapObjectiveNameResource(BaseService<MapObjectiveName> service) {
        super(service);
    }
}