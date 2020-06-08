package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, String> {
}
