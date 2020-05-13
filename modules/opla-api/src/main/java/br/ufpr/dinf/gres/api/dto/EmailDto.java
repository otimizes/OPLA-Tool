package br.ufpr.dinf.gres.api.dto;

public class EmailDto {
    public String token;
    public String to;
    public String subject;
    public String text;
    public String file;

    public EmailDto(String token, String to, String subject, String text) {
        this.token = token;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public EmailDto(String token, String subject, String text) {
        this.token = token;
        this.subject = subject;
        this.text = text;
    }
}
