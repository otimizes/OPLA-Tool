package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.entity.*;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends BaseService<User> {

    private final UserRepository repository;
    private final EmailService emailService;

    public UserService(UserRepository repository, EmailService emailService) {
        super(repository);
        this.repository = repository;
        this.emailService = emailService;
    }

    @Transactional
    public LoginResultDto forgot(LoginDto loginDto) {
        LoginResultDto sigin = sigin(loginDto);
        if (sigin.status.equals(LoginStatusDto.WRONG_PASSWORD)) {
            sigin.user.setPassword(RandomStringUtils.randomNumeric(5));
            sigin.user = repository.save(sigin.user);
            emailService.send(new EmailDto(sigin.user.getLogin(), "Your OPLA-Tool password was changed.", "The new password is: " + sigin.user.getPassword()));
            sigin.status = LoginStatusDto.CHANGED_PASSWORD;
            return sigin;
        }
        return sigin;
    }

    @Transactional
    public LoginResultDto sigin(LoginDto loginDto) {
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
