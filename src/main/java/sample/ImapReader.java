package sample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class ImapReader {

    String host = "imap.gmail.com";
    String username = "username@gmail.com";
    String password = "password";

    public static void main(String[] args) throws Exception {
        ImapReader instance = new ImapReader();
        instance.username = args[0];
        instance.password = args[1];
        instance.browse();
    }

    private void browse() throws MessagingException, IOException {
        Session session = createSession();
        Store store = session.getStore("imaps");
        store.connect(host, username, password);

        Folder root = store.getDefaultFolder();
        for(Folder folder : root.list()) {
            System.out.println("Folder: " + folder.getFullName());
        }

        Folder inbox = root.getFolder("INBOX");
        if(inbox.exists()) {
            if(!inbox.isOpen()) {
                inbox.open(Folder.READ_ONLY);
            }
            Message[] messages = inbox.getMessages();
            System.out.println("Inbox size: " + messages.length);
            for(Message message : messages) {
                System.out.println("- " + message.getSubject());
            }
            if(messages.length > 0) {
                dump(messages[0]);
            }
        }
    }

    private void dump(Message message) throws MessagingException, IOException {
        System.out.println("--- message.getAllHeaders()");
        Enumeration<?> headers = message.getAllHeaders();
        while(headers.hasMoreElements()) {
            Header header = (Header) headers.nextElement();
            System.out.println(header.getName() + ": " + header.getValue());
        }
        System.out.println("--- message.getDataHandler().writeTo(stm) ");
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        message.getDataHandler().writeTo(stm);
        System.out.println(new String(stm.toByteArray(), "UTF-8"));
        System.out.println("--- message.writeTo(stm)");
        stm = new ByteArrayOutputStream();
        message.writeTo(stm);
        System.out.println(new String(stm.toByteArray(), "UTF-8"));
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
