package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.CMObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cm-objective-function")
public class CMObjectiveFunctionResource extends BaseResource<CMObjectiveFunction> {

    public CMObjectiveFunctionResource(BaseService<CMObjectiveFunction> service) {
        super(service);
    }
}