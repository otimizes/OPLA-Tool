package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.LCCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lcc-objective-function")
public class LCCObjectiveFunctionResource extends BaseResource<LCCObjectiveFunction> {

    public LCCObjectiveFunctionResource(BaseService<LCCObjectiveFunction> service) {
        super(service);
    }
}