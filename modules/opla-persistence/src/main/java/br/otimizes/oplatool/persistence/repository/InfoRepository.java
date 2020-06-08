package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<Info, String> {
}
