package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.DCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dc-objective-function")
public class DCObjectiveFunctionResource extends BaseResource<DCObjectiveFunction> {

    public DCObjectiveFunctionResource(BaseService<DCObjectiveFunction> service) {
        super(service);
    }
}