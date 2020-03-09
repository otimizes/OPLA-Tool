package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.domain.entity.metric.FeatureDrivenMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureDrivenMetricRepository extends JpaRepository<FeatureDrivenMetric, String> {
}
