package sample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ImapCreator {

    String host = "imap.gmail.com";
    String username = "username@gmail.com";
    String password = "password";
    String file = "mail.txt";
    String from = "username1@gmail.com";
    String to = "username2@gmail.com";
    String subject = "Test: IMAP";
    String body = "Hallo\n\nDies ist ein Test von javax.mail.*\n\nBye Bye";

    public static void main(String[] args) throws Exception {
        ImapCreator instance = new ImapCreator();
        instance.username = args[0];
        instance.password = args[1];
        instance.file = args[2];
        instance.create();
    }

    private void create() throws MessagingException, IOException {
        Session session = createSession();
        Store store = session.getStore("imaps");
        store.connect(host, username, password);

        Folder root = store.getDefaultFolder();
        Folder inbox = root.getFolder("INBOX");
        if(inbox.exists()) {
            if(!inbox.isOpen()) {
                inbox.open(Folder.READ_ONLY);
            }
            createMessage(session, inbox);
            loadMessage(session, inbox);
        }
    }

    private void createMessage(Session session, Folder folder) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        folder.appendMessages(new Message[] { message });
    }

    private void loadMessage(Session session, Folder folder) throws MessagingException, IOException {
        Message message = new MimeMessage(session, new ByteArrayInputStream(Files.readAllBytes(Paths.get(file))));
        message.setHeader("X-AM-I-COOL", "YES");
        message.setSentDate(new Date());
        folder.appendMessages(new Message[] { message });
    }

    private Session createSession() {
        return Session.getInstance(createProperties(), null);
    }

    private Properties createProperties() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        return props;
    }
}
