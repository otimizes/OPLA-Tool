package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.MapObjectiveName;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map-objective-name")
public class MapObjectiveNameResource extends BaseResource<MapObjectiveName> {

    public MapObjectiveNameResource(BaseService<MapObjectiveName> service) {
        super(service);
    }
}