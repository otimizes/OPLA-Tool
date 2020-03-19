package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tv-objective-function")
public class TVObjectiveFunctionResource extends BaseResource<TVObjectiveFunction> {

    public TVObjectiveFunctionResource(BaseService<TVObjectiveFunction> service) {
        super(service);
    }
}