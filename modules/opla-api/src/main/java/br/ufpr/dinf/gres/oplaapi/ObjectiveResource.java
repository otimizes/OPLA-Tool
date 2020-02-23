package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.Objective;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/objective")
public class ObjectiveResource extends BaseResource<Objective> {

    public ObjectiveResource(BaseService<Objective> service) {
        super(service);
    }
}