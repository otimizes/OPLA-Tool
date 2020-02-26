package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
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