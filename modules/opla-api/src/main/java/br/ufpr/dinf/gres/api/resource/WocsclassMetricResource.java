package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.metric.WocsclassMetric;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wocs-class-metric")
public class WocsclassMetricResource extends BaseResource<WocsclassMetric> {

    public WocsclassMetricResource(BaseService<WocsclassMetric> service) {
        super(service);
    }
}