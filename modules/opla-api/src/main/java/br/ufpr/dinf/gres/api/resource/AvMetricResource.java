package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/av-metric")
public class AvMetricResource extends BaseResource<TVObjectiveFunction> {

    public AvMetricResource(BaseService<TVObjectiveFunction> service) {
        super(service);
    }
}