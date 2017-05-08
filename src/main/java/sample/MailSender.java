package sample;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSender {

    String username = "username@gmail.com";
    String password = "password";
    String from = "username@gmail.com";
    String to = "username@gmail.com";
    String subject = "Test: MIME multipart (text, html and attachments) and headers";
    String body = "Hallo\n\nDies ist ein Test von javax.mail.*\n\nBye Bye";
    String bodyHtml = "<html><body><p><b><u>Hallo</u></b><br><br>Dies ist ein Test von javax.mail.*<br><br>Bye Bye</p></body></html>";

    public static void main(String[] args) throws Exception {
        MailSender instance = new MailSender();
        instance.username = args[0];
        instance.password = args[1];
        instance.from = args[2];
        instance.to = args[3];
        instance.sendHtmlMessage();
    }

    private void sendTextMessage() throws AddressException, MessagingException {
        Session session = createSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        System.out.println("Sending..");
        Transport.send(message);
        System.out.println("Sent.");
    }

    private void sendHtmlMessage() throws AddressException, MessagingException, IOException {
        Session session = createSession();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setHeader("X-FOO", "BAR");

        MimeMultipart multiPart = new MimeMultipart();
        message.setContent(multiPart);

        MimeBodyPart innerPart = new MimeBodyPart();
        multiPart.addBodyPart(innerPart);
        MimeMultipart multiPartInner = new MimeMultipart("alternative");
        innerPart.setContent(multiPartInner);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body, "utf-8");
        multiPartInner.addBodyPart(textPart);

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(bodyHtml, "text/html; charset=utf-8");
        multiPartInner.addBodyPart(htmlPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(new FileDataSource("src/main/resources/p16252.jpg")));
        attachmentPart.setFileName("ich.jpeg");
        multiPart.addBodyPart(attachmentPart);

        System.out.println("Sending..");
        Transport.send(message);
        System.out.println("Sent.");
    }

    private Session createSession() {
        Authenticator authenticator = new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Using credentials.");
                return new PasswordAuthentication(username, password);
            }
        };

        return Session.getInstance(createProperties(), authenticator);
    }

    private Properties createProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }
}
