package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/elegance-metric")
public class EleganceMetricResource extends BaseResource<ELEGObjectiveFunction> {

    public EleganceMetricResource(BaseService<ELEGObjectiveFunction> service) {
        super(service);
    }
}