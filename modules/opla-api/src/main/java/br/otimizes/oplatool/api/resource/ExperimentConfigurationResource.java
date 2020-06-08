package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.ExperimentConfiguration;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/experiment-configuration")
public class ExperimentConfigurationResource extends BaseResource<ExperimentConfiguration> {

    public ExperimentConfigurationResource(BaseService<ExperimentConfiguration> service) {
        super(service);
    }
}