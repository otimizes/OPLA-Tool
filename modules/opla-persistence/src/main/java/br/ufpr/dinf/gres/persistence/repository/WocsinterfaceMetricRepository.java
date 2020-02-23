package br.ufpr.dinf.gres.persistence.repository;

import br.ufpr.dinf.gres.opla.entity.metric.WocsinterfaceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WocsinterfaceMetricRepository extends JpaRepository<WocsinterfaceMetric, Long> {
}
