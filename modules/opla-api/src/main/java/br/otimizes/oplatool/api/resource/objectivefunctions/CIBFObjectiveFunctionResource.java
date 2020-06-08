package br.otimizes.oplatool.api.resource.objectivefunctions;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.objectivefunctions.CIBFObjectiveFunction;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cibf-objective-function")
public class CIBFObjectiveFunctionResource extends BaseResource<CIBFObjectiveFunction> {

    public CIBFObjectiveFunctionResource(BaseService<CIBFObjectiveFunction> service) {
        super(service);
    }
}