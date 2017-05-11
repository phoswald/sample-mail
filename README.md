# sample-mail
Experiments with javax.mail

## Usage
    $ mvn clean verify
    $ java -cp "target/sample-mail.jar:target/lib/*" sample.SmtpSender <username@gmail.com> <password> <from@gmail.com> <to@domain.com>
    $ java -cp "target/sample-mail.jar:target/lib/*" sample.ImapReader <username@gmail.com> <password>
    $ java -cp "target/sample-mail.jar:target/lib/*" sample.ImapCreator <username@gmail.com> <password> <file.eml>
    