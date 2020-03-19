package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fm-objective-function")
public class FMObjectiveFunctionResource extends BaseResource<FMObjectiveFunction> {
    public FMObjectiveFunctionResource(BaseService<FMObjectiveFunction> service) {
        super(service);
    }
}