package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feature-driven-metric")
public class FeatureDrivenMetricResource extends BaseResource<FMObjectiveFunction> {
    public FeatureDrivenMetricResource(BaseService<FMObjectiveFunction> service) {
        super(service);
    }
}