package br.otimizes.oplatool.persistence.service;

import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationFileConfig;
import br.otimizes.oplatool.domain.entity.EmailDto;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * https://mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 * Access acount and enable external app password
 * https://myaccount.google.com/security
 */
@Service
public class EmailService {


    public void send(EmailDto emailDto) {
        final String username = ApplicationFileConfig.getInstance().getEmailUser();
        final String password = ApplicationFileConfig.getInstance().getEmailPassword();

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailDto.to)
            );
            message.setSubject(emailDto.subject);
            message.setText(emailDto.text);


            if (emailDto.file != null) {
                Multipart multipart = new MimeMultipart();

                ZipFiles zipFiles = new ZipFiles();
                File file = zipFiles.zipDirectory(OPLAThreadScope.userDir.get() + emailDto.file);

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(emailDto.file + "zip");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }

            Transport.send(message);

            System.out.println("E-mail sent from " + username + " to " + emailDto.to);

        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}