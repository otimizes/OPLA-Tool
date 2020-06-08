package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fm-objective-function")
public class FMObjectiveFunctionResource extends BaseResource<FMObjectiveFunction> {
    public FMObjectiveFunctionResource(BaseService<FMObjectiveFunction> service) {
        super(service);
    }
}