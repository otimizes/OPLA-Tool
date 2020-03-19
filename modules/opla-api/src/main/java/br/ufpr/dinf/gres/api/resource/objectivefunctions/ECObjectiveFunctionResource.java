package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ECObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ec-objective-function")
public class ECObjectiveFunctionResource extends BaseResource<ECObjectiveFunction> {

    public ECObjectiveFunctionResource(BaseService<ECObjectiveFunction> service) {
        super(service);
    }
}