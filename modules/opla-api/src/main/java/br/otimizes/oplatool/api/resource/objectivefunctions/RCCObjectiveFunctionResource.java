package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rcc-objective-function")
public class RCCObjectiveFunctionResource extends BaseResource<RCCObjectiveFunction> {

    public RCCObjectiveFunctionResource(BaseService<RCCObjectiveFunction> service) {
        super(service);
    }
}