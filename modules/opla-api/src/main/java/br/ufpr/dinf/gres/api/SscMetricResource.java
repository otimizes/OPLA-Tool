package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.metric.SscMetric;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ssc-metric")
public class SscMetricResource extends BaseResource<SscMetric> {

    public SscMetricResource(BaseService<SscMetric> service) {
        super(service);
    }
}