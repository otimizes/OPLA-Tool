package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.TAMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tam-objective-function")
public class TAMObjectiveFunctionResource extends BaseResource<TAMObjectiveFunction> {

    public TAMObjectiveFunctionResource(BaseService<TAMObjectiveFunction> service) {
        super(service);
    }
}