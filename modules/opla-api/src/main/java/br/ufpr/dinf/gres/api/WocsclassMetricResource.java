package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.WocsclassMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wocs-class-metric")
public class WocsclassMetricResource extends BaseResource<WocsclassMetric> {

    public WocsclassMetricResource(BaseService<WocsclassMetric> service) {
        super(service);
    }
}