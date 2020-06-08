package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.EXTObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ext-objective-function")
public class EXTObjectiveFunctionResource extends BaseResource<EXTObjectiveFunction> {

    public EXTObjectiveFunctionResource(BaseService<EXTObjectiveFunction> service) {
        super(service);
    }
}