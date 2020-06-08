package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.DistanceEuclidean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceEuclideanRepository extends JpaRepository<DistanceEuclidean, String> {
}
