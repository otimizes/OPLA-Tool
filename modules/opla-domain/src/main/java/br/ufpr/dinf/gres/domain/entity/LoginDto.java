package br.ufpr.dinf.gres.domain.entity;

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
