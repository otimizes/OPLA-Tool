package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.metric.CbcsMetric;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cbcs-metric")
public class CbcsMetricResource extends BaseResource<CbcsMetric> {

    public CbcsMetricResource(BaseService<CbcsMetric> service) {
        super(service);
    }
}