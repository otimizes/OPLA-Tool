package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CMObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conventional-metric")
public class ConventionalMetricResource extends BaseResource<CMObjectiveFunction> {

    public ConventionalMetricResource(BaseService<CMObjectiveFunction> service) {
        super(service);
    }
}