package sit.int221.sas.sit_announcement_system_backend.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sit.int221.sas.sit_announcement_system_backend.properties.MailProperties;

@Component
public class MailComponent {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties ;
    @Async
    public void sendmail(String emailTo , String subject , String content) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(emailTo);
            helper.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            mailSender.send(message);
        }
        catch (Exception e){
            throw new  MessagingException(e.getMessage()) ;
        }
    }
}
