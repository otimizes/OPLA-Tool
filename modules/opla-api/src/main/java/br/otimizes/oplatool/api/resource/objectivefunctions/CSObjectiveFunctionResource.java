package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.CSObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cs-objective-function")
public class CSObjectiveFunctionResource extends BaseResource<CSObjectiveFunction> {

    public CSObjectiveFunctionResource(BaseService<CSObjectiveFunction> service) {
        super(service);
    }
}