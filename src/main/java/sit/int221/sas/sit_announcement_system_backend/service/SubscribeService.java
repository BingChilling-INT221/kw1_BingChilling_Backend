package sit.int221.sas.sit_announcement_system_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;
import sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder.CompositekeySubscrib;
import sit.int221.sas.sit_announcement_system_backend.properties.MailProperties;
import sit.int221.sas.sit_announcement_system_backend.repository.CategoryRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.SubscribeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class SubscribeService {
    @Autowired
    SubscribeRepository subscribeRepository ;
    @Autowired
    CategoryRepository categoryRepository ;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties ;

    public List<Subscribe> getSubscribes (){
        return  subscribeRepository.findAll();
    }

    public List<Integer> AddSubScribe(String email,List<Integer> categorys){

       List<Subscribe> subscribesObjOfEmail = subscribeRepository.findByEmail(email);
       List<Integer> tempCategory = new ArrayList<>();
        System.out.println("AddSubScribe 1");
        System.out.println(subscribesObjOfEmail);
        categorys.forEach((catagory)->{
            System.out.println("AddSubScribe 2");
            tempCategory.add(catagory) ;
            System.out.println("AddSubScribe 3");
            if(subscribesObjOfEmail.size()!=0){
            subscribesObjOfEmail.forEach((subscribe)->{
                    if(subscribe.getCategory().getCategoryId().equals(catagory)){
                        tempCategory.remove(tempCategory.size()-1);
                        System.out.println("check sub");
                    }
                    if(((subscribesObjOfEmail.indexOf(subscribe) == subscribesObjOfEmail.size()-1) && tempCategory.size()>0)){
                        if(catagory.equals(tempCategory.get(tempCategory.size() - 1))) {
                            System.out.println(tempCategory.get(tempCategory.size() - 1));
                            Category categoryTarget = categoryRepository.findById(tempCategory.get(tempCategory.size() - 1)).orElse(null);
                            System.out.println("add sub");
                            assert categoryTarget != null;
                            System.out.println(categoryTarget.getCategoryName());
                            Subscribe subscribe1=subscribeRepository.saveAndFlush(new Subscribe(email, categoryTarget));
                            System.out.println(subscribe1);
                            tempCategory.clear();
                        }
                    }

                });}
            else {
                Category categoryTarget = categoryRepository.findById(catagory).orElse(null);
                subscribeRepository.saveAndFlush(new Subscribe(email, categoryTarget));
            }

        });
//        tempCategory.forEach((x)-> {
//            subscribeRepository.saveAndFlush(new Subscribe(email,x)) ;
//        });
        return tempCategory;
    }
    public Integer sendOTP(String emailTo,String subject) throws MessagingException {
        Random random = new Random() ;
        int otp = random.nextInt(900000)+100000;
        System.out.println("OTP"+otp);
        String htmlContent = "<h3>OTP For YourEmail </h3>" +
                "<p>You have registered to send notification of SAS WEB. This is your OTP : " + otp + "</p>" +
                "<hr>  <p> __ please  keep this for secrete and this code will expired in 5 minutes after receiving this mail __!! </p>";
        sendmail(emailTo,subject,htmlContent);
//        helper.setText("You have registered to send notification of SAS WEB. This is your OTP :  <br> ${otp} <\br>   \n please keep this be secrete");
        return otp ;
    }

    public void sendEmailToNotificationSubscribe(String emailTo,String subject,String body) throws MessagingException {
        try {
            sendmail(emailTo,subject, body + getFooterOfEmailNotification(emailTo));
        }catch (Exception e){
            throw new  MessagingException(e.getMessage()) ;
        }

    }



    public List<Subscribe> deleteSubscribeByEmailAndCategoryId(String emailTo , Integer[] categoryId){
        List<Subscribe>  subscribes = subscribeRepository.findByEmailAndCategory_CategoryId(emailTo,categoryId) ;
        subscribeRepository.deleteAll(subscribes);
        return subscribes ;
    }
    public List<Subscribe> deleteAllSubscribe(String emailTo){
        List<Subscribe>  subscribes = subscribeRepository.findByEmail(emailTo) ;
        subscribeRepository.deleteAll(subscribes);
        return  subscribes ;

    }

    public void sendEmailToNotificationSubscribeWhenAnnouncementUpdated(String subject, Announcement announcement) throws MessagingException {
        try {
            StringBuilder htmlContent = new StringBuilder("<h3>Notification from Your Category Subscribed</h3>" +
                    "<p>An announcement from the work category has been updated by a user named "+   announcement.getAnnouncementOwner().getName()  +" </p>  ");
            List<Subscribe> subscribesFromCategories=subscribeRepository.findByCategory_CategoryId(announcement.getAnnouncementCategory().getCategoryId());
            for (Subscribe x: subscribesFromCategories) {
                sendmail(x.getEmail(),subject, htmlContent.toString()+ getFooterOfEmailNotification(x.getEmail()));
            }
            
        }catch (Exception e){
            throw new  MessagingException(e.getMessage()) ;
        }

    }

    public String getFooterOfEmailNotification(String emailTo){
        System.out.println(emailTo);
        StringBuilder htmlContent = new StringBuilder("<div> คุณได้มีการติดตาม category ของระบบทั้งหมดดังนี้ <ul>") ;
        List<Subscribe> subscribesFromTargetEmail  = subscribeRepository.findByEmail(emailTo);
        System.out.println(subscribesFromTargetEmail.size());
        for(Subscribe x : subscribesFromTargetEmail ){
            htmlContent.append("<li> ").append(x.getCategory().getCategoryName()).append(" </li> ");
        }
        htmlContent.append(" </ul> </div> <p>  <a href=\"https://intproj22.sit.kmutt.ac.th/kw1/unsubscribe/email?email= ").append(emailTo).append(" \" > Click to Manage your subscribe. >> </a> </p>  ");
        System.out.println(htmlContent);
        return  htmlContent.toString() ;
    }
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
