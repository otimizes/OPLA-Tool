package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.entity.ExperimentConfiguration;
import br.ufpr.dinf.gres.api.base.BaseResource;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiment-configuration")
public class ExperimentConfigurationResource extends BaseResource<ExperimentConfiguration> {

    public ExperimentConfigurationResource(BaseService<ExperimentConfiguration> service) {
        super(service);
    }
}