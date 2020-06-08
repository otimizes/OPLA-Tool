package br.otimizes.oplatool.persistence.repository;

import br.otimizes.oplatool.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByLogin(String login);
    List<User> findAllByToken(String token);
}
