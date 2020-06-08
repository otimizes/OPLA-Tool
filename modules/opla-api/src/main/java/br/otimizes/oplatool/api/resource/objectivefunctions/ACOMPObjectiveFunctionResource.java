package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ACOMPObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/acomp-objective-function")
public class ACOMPObjectiveFunctionResource extends BaseResource<ACOMPObjectiveFunction> {

    public ACOMPObjectiveFunctionResource(BaseService<ACOMPObjectiveFunction> service) {
        super(service);
    }
}