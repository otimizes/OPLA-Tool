package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.ExperimentConfiguration;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiment-configuration")
public class ExperimentConfigurationResource extends BaseResource<ExperimentConfiguration> {

    public ExperimentConfigurationResource(BaseService<ExperimentConfiguration> service) {
        super(service);
    }
}