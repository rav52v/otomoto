package main.tools;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    private ConfigurationParser config;
    private String to;
    private String from;
    private String username;
    private String password;
    private String host;

    private Properties props;
    private Session session;
    private Message message;

    public Email() {
        config = new ConfigurationParser();
        to = config.getReceiverEmail();
        from = config.getSenderEmail();
        username = config.getLogin();
        password = config.getPassword();
        host = "smtp.gmail.com";

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        message = new MimeMessage(session);
    }

    public void sendEmail(String subject, String value) {
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(value);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}