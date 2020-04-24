package br.ufpr.dinf.gres.persistence.service;

import br.ufpr.dinf.gres.domain.entity.LoginDto;
import br.ufpr.dinf.gres.domain.entity.LoginResultDto;
import br.ufpr.dinf.gres.domain.entity.LoginStatusDto;
import br.ufpr.dinf.gres.domain.entity.User;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService<User> {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public LoginResultDto login(LoginDto loginDto) {
        List<User> allByLoginAndPassword = repository.findAllByLogin(loginDto.login);
        if (allByLoginAndPassword.size() > 0) {
            if (allByLoginAndPassword.get(0).getPassword().equals(loginDto.password)) {
                return new LoginResultDto(allByLoginAndPassword.get(0), LoginStatusDto.LOGGED);
            } else {
                return new LoginResultDto(allByLoginAndPassword.get(0), LoginStatusDto.WRONG_PASSWORD);
            }
        }
        return new LoginResultDto(repository.save(new User(loginDto.login, loginDto.password, getTokenByLogin(loginDto))), LoginStatusDto.CREATED);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        List<User> allByLogin = repository.findAllByLogin(email);
        return allByLogin.size() > 0 ? allByLogin.get(0) : null;
    }

    @Transactional(readOnly = true)
    public User findUserByToken(String token) {
        List<User> allByToken = repository.findAllByToken(token);
        return allByToken.size() > 0 ? allByToken.get(0) : null;
    }

    private String getTokenByLogin(LoginDto loginDto) {
        return DigestUtils.md5DigestAsHex((loginDto.login + loginDto.password + LocalDateTime.now().toString()).getBytes());
    }
}
