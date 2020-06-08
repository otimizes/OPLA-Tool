package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.ExperimentConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentConfigurationRepository extends JpaRepository<ExperimentConfiguration, String> {
}
