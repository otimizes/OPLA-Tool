package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.Objective;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/objective")
public class ObjectiveResource extends BaseResource<Objective> {

    public ObjectiveResource(BaseService<Objective> service) {
        super(service);
    }
}