package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, String> {
}
