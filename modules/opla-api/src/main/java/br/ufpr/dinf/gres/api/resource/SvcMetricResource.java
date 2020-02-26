package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.metric.SvcMetric;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/svc-metric")
public class SvcMetricResource extends BaseResource<SvcMetric> {

    public SvcMetricResource(BaseService<SvcMetric> service) {
        super(service);
    }
}