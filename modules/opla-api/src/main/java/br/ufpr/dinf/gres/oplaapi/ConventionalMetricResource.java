package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conventional-metric")
public class ConventionalMetricResource extends BaseResource<ConventionalMetric> {

    public ConventionalMetricResource(BaseService<ConventionalMetric> service) {
        super(service);
    }
}