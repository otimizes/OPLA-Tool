package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.FDACObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fdac-objective-function")
public class FDACObjectiveFunctionResource extends BaseResource<FDACObjectiveFunction> {

    public FDACObjectiveFunctionResource(BaseService<FDACObjectiveFunction> service) {
        super(service);
    }
}