package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elegance-metric")
public class EleganceMetricResource extends BaseResource<EleganceMetric> {

    public EleganceMetricResource(BaseService<EleganceMetric> service) {
        super(service);
    }
}