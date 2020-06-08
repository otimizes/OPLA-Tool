package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.WOCSCLASSObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wocs-class-objective-function")
public class WOCSCLASSObjectiveFunctionResource extends BaseResource<WOCSCLASSObjectiveFunction> {

    public WOCSCLASSObjectiveFunctionResource(BaseService<WOCSCLASSObjectiveFunction> service) {
        super(service);
    }
}