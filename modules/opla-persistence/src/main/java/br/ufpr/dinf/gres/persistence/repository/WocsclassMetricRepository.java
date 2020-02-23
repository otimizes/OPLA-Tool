package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.opla.entity.metric.WocsclassMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WocsclassMetricRepository extends JpaRepository<WocsclassMetric, Long> {
}
