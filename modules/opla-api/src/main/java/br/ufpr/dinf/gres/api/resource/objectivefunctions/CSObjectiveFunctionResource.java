package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CSObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cs-objective-function")
public class CSObjectiveFunctionResource extends BaseResource<CSObjectiveFunction> {

    public CSObjectiveFunctionResource(BaseService<CSObjectiveFunction> service) {
        super(service);
    }
}