package br.otimizes.oplatool.domain.entity;

public class LoginResultDto {
    public User user;
    public LoginStatusDto status;

    public LoginResultDto() {
    }

    public LoginResultDto(User user, LoginStatusDto status) {
        this.user = user;
        this.status = status;
    }
}
