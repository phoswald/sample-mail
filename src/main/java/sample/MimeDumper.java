package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

public class MimeDumper {

    String file = "mail.eml";

    public static void main(String[] args) throws Exception {
        MimeDumper instance = new MimeDumper();
        instance.file = args[0];
        instance.dump();
    }

    private void dump() throws MessagingException, IOException {
        Session session = createSession();
        Store store = session.getStore("imaps");
        MimeMessage message = new MimeMessage(session, Files.newInputStream(Paths.get(file)));
        dumpContent(message);
    }

    private void dumpContent(Part message) throws IOException, MessagingException {
        System.out.println("\n\n\n--- message --- " + message.getContent().getClass().getName());
        Enumeration<?> headers = message.getAllHeaders();
        while(headers.hasMoreElements()) {
            Header header = (Header) headers.nextElement();
            System.out.println(header.getName() + ": " + header.getValue());
        }
        Object content = message.getContent();
        if(content instanceof String) {
            System.out.println(content);
        } else if(content instanceof Multipart) {
            int count = ((Multipart) content).getCount();
            for(int i = 0; i < count; i++) {
                BodyPart part = ((Multipart) content).getBodyPart(i);
                dumpContent(part);
            }
        }
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
