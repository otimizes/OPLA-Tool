package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.LCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lcc-objective-function")
public class LCCObjectiveFunctionResource extends BaseResource<LCCObjectiveFunction> {

    public LCCObjectiveFunctionResource(BaseService<LCCObjectiveFunction> service) {
        super(service);
    }
}