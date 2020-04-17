package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rcc-objective-function")
public class RCCObjectiveFunctionResource extends BaseResource<RCCObjectiveFunction> {

    public RCCObjectiveFunctionResource(BaseService<RCCObjectiveFunction> service) {
        super(service);
    }
}