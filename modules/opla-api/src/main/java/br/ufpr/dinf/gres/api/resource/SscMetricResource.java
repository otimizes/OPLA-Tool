package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SDObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ssc-metric")
public class SscMetricResource extends BaseResource<SDObjectiveFunction> {

    public SscMetricResource(BaseService<SDObjectiveFunction> service) {
        super(service);
    }
}