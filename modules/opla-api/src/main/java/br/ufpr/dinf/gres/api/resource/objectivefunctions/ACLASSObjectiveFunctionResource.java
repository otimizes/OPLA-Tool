package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ACLASSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aclass-objective-function")
public class ACLASSObjectiveFunctionResource extends BaseResource<ACLASSObjectiveFunction> {

    public ACLASSObjectiveFunctionResource(BaseService<ACLASSObjectiveFunction> service) {
        super(service);
    }
}