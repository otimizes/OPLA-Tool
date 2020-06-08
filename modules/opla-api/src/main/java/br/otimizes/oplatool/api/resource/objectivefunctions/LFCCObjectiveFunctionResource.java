package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.LFCCObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lfcc-objective-function")
public class LFCCObjectiveFunctionResource extends BaseResource<LFCCObjectiveFunction> {

    public LFCCObjectiveFunctionResource(BaseService<LFCCObjectiveFunction> service) {
        super(service);
    }
}