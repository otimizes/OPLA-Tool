package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.AvMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/av-metric")
public class AvMetricResource extends BaseResource<AvMetric> {

    public AvMetricResource(BaseService<AvMetric> service) {
        super(service);
    }
}