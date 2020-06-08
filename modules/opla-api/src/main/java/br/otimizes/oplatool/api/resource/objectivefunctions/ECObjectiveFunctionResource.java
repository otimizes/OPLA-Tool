package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ECObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ec-objective-function")
public class ECObjectiveFunctionResource extends BaseResource<ECObjectiveFunction> {

    public ECObjectiveFunctionResource(BaseService<ECObjectiveFunction> service) {
        super(service);
    }
}