package br.ufpr.dinf.gres.api.resource.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eleg-objective-function")
public class ELEGObjectiveFunctionResource extends BaseResource<ELEGObjectiveFunction> {

    public ELEGObjectiveFunctionResource(BaseService<ELEGObjectiveFunction> service) {
        super(service);
    }
}