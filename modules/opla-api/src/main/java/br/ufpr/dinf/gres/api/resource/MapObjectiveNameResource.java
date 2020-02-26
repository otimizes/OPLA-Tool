package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.MapObjectiveName;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map-objective-name")
public class MapObjectiveNameResource extends BaseResource<MapObjectiveName> {

    public MapObjectiveNameResource(BaseService<MapObjectiveName> service) {
        super(service);
    }
}