package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiment")
public class ExperimentResource extends BaseResource<Experiment> {

    public ExperimentResource(BaseService<Experiment> service) {
        super(service);
    }
}