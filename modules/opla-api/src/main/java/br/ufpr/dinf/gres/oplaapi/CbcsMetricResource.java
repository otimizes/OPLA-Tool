package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.metric.CbcsMetric;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cbcs-metric")
public class CbcsMetricResource extends BaseResource<CbcsMetric> {

    public CbcsMetricResource(BaseService<CbcsMetric> service) {
        super(service);
    }
}