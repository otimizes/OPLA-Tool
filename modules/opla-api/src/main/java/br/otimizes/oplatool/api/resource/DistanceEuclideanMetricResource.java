package br.otimizes.oplatool.api.resource;

import br.otimizes.oplatool.api.base.BaseResource;
import br.otimizes.oplatool.domain.entity.DistanceEuclidean;
import br.otimizes.oplatool.persistence.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/distance-euclidean")
public class DistanceEuclideanMetricResource extends BaseResource<DistanceEuclidean> {

    public DistanceEuclideanMetricResource(BaseService<DistanceEuclidean> service) {
        super(service);
    }
}