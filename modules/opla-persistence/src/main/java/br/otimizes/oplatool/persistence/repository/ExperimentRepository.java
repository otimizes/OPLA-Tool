package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, String> {
}
