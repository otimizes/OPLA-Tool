package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiment")
public class ExperimentResource extends BaseResource<Experiment> {

    public ExperimentResource(BaseService<Experiment> service) {
        super(service);
    }
}