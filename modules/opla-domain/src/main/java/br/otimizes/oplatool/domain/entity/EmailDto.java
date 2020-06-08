package br.otimizes.oplatool.domain.entity;

public class EmailDto {
    public String to;
    public String subject;
    public String text;
    public String file;

    public EmailDto(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
