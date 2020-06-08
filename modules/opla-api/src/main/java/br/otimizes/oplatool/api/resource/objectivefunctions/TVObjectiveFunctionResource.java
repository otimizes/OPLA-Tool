package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tv-objective-function")
public class TVObjectiveFunctionResource extends BaseResource<TVObjectiveFunction> {

    public TVObjectiveFunctionResource(BaseService<TVObjectiveFunction> service) {
        super(service);
    }
}