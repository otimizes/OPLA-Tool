package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PLAExtensibilityMetricRepository extends JpaRepository<PLAExtensibilityMetric, Long> {
}