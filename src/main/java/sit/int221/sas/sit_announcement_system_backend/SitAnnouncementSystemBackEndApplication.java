package sit.int221.sas.sit_announcement_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import sit.int221.sas.sit_announcement_system_backend.properties.JwtProperties;
import sit.int221.sas.sit_announcement_system_backend.properties.MailProperties;

@EnableConfigurationProperties({JwtProperties.class, MailProperties.class})
@SpringBootApplication
public class SitAnnouncementSystemBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SitAnnouncementSystemBackEndApplication.class, args);
    }


}
