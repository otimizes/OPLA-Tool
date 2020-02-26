package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.ExperimentConfiguration;
import br.ufpr.dinf.gres.api.config.BaseResource;
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