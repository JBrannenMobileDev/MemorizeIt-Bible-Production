package nape.biblememory.utils;

/**
 * Created by jbrannen on 9/25/17.
 */

import android.content.Context;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import nape.biblememory.R;

public class SendEmail {
    private String from = "jbinvestments15@gmail.com";
    private String password;
    private String to = "memorizeitbible@gmail.com";

    public void sendEmail(String subject, String msg, Context context) {
        password = context.getResources().getString(R.string.pass_part1) + context.getResources().getString(R.string.pass_part2);
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mai l.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject);
            message.setText(msg);
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }
}