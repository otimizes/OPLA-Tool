package br.otimizes.oplatool.domain.entity;

public class LoginDto {
    public String login;
    public String password;

    public LoginDto() {
    }

    public LoginDto(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
