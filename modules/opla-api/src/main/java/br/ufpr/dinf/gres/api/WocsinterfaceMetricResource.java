package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.WocsinterfaceMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wocs-interface-metric")
public class WocsinterfaceMetricResource extends BaseResource<WocsinterfaceMetric> {

    public WocsinterfaceMetricResource(BaseService<WocsinterfaceMetric> service) {
        super(service);
    }
}