package sit.int221.sas.sit_announcement_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import sit.int221.sas.sit_announcement_system_backend.properties.FileStorageProperties;
import sit.int221.sas.sit_announcement_system_backend.properties.JwtProperties;
import sit.int221.sas.sit_announcement_system_backend.properties.MailProperties;

@EnableAsync
@EnableConfigurationProperties({JwtProperties.class, MailProperties.class, FileStorageProperties.class})
@SpringBootApplication
public class SitAnnouncementSystemBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SitAnnouncementSystemBackEndApplication.class, args);
    }


}
