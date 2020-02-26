package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pla-extensibility-metric")
public class PLAExtensibilityMetricResource extends BaseResource<PLAExtensibilityMetric> {

    public PLAExtensibilityMetricResource(BaseService<PLAExtensibilityMetric> service) {
        super(service);
    }
}