package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.dto.EmailDto;
import br.ufpr.dinf.gres.api.resource.ZipFiles;
import br.ufpr.dinf.gres.architecture.config.ApplicationFile;
import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScope;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.domain.entity.User;
import br.ufpr.dinf.gres.persistence.service.UserService;
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

    private final UserService userService;

    public EmailService(UserService userService) {
        this.userService = userService;
    }

    public void send(EmailDto emailDto) {
        User userByEmail = userService.findUserByToken(emailDto.token);
        if (emailDto.to == null) emailDto.to = userByEmail.getLogin();
        if (!userByEmail.getToken().equals(OPLAThreadScope.token.get()))
            throw new RuntimeException("You are not allowed to do this.");
        final String username = ApplicationFile.getInstance().getConfig().getEmailUser();
        final String password = ApplicationFile.getInstance().getConfig().getEmailPassword();

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
            message.setFrom(new InternetAddress(userByEmail.getLogin()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailDto.to)
            );
            message.setSubject(emailDto.subject);
            message.setText(emailDto.text);


            if (emailDto.file != null) {
                Multipart multipart = new MimeMultipart();

                ZipFiles zipFiles = new ZipFiles();
                File file = zipFiles.zipDirectory(OPLAConfigThreadScope.userDir.get() + emailDto.file);

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(emailDto.file + "zip");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }

            Transport.send(message);

            System.out.println("E-mail sent from " + userByEmail.getLogin() + " to " + emailDto.to);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}