package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoResource extends BaseResource<Info> {

    public InfoResource(BaseService<Info> service) {
        super(service);
    }
}