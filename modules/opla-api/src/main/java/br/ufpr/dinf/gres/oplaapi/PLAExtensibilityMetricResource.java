package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pla-extensibility-metric")
public class PLAExtensibilityMetricResource extends BaseResource<PLAExtensibilityMetric> {

    public PLAExtensibilityMetricResource(BaseService<PLAExtensibilityMetric> service) {
        super(service);
    }
}