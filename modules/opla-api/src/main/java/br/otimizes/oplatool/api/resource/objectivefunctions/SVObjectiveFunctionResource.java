package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.SVObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sv-objective-function")
public class SVObjectiveFunctionResource extends BaseResource<SVObjectiveFunction> {

    public SVObjectiveFunctionResource(BaseService<SVObjectiveFunction> service) {
        super(service);
    }
}