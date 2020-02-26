package br.ufpr.dinf.gres.api;

import br.ufpr.dinf.gres.domain.entity.DistanceEuclidean;
import br.ufpr.dinf.gres.api.config.BaseResource;
import br.ufpr.dinf.gres.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/distance-euclidean")
public class DistanceEuclideanMetricResource extends BaseResource<DistanceEuclidean> {

    public DistanceEuclideanMetricResource(BaseService<DistanceEuclidean> service) {
        super(service);
    }
}