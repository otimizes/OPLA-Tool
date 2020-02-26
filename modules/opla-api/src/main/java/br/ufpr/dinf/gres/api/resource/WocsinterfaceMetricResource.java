package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.metric.WocsinterfaceMetric;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wocs-interface-metric")
public class WocsinterfaceMetricResource extends BaseResource<WocsinterfaceMetric> {

    public WocsinterfaceMetricResource(BaseService<WocsinterfaceMetric> service) {
        super(service);
    }
}