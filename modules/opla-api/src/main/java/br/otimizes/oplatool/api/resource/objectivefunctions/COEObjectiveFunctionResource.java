package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.COEObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coe-objective-function")
public class COEObjectiveFunctionResource extends BaseResource<COEObjectiveFunction> {

    public COEObjectiveFunctionResource(BaseService<COEObjectiveFunction> service) {
        super(service);
    }
}