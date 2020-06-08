package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eleg-objective-function")
public class ELEGObjectiveFunctionResource extends BaseResource<ELEGObjectiveFunction> {

    public ELEGObjectiveFunctionResource(BaseService<ELEGObjectiveFunction> service) {
        super(service);
    }
}