package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.metric.SvcMetric;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/svc-metric")
public class SvcMetricResource extends BaseResource<SvcMetric> {

    public SvcMetricResource(BaseService<SvcMetric> service) {
        super(service);
    }
}