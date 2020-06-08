package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/execution")
public class ExecutionResource extends BaseResource<Execution> {

    public ExecutionResource(BaseService<Execution> service) {
        super(service);
    }
}