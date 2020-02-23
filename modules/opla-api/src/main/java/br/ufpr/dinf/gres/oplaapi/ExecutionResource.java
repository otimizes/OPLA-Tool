package br.ufpr.dinf.gres.oplaapi;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/execution")
public class ExecutionResource extends BaseResource<Execution> {

    public ExecutionResource(BaseService<Execution> service) {
        super(service);
    }
}