package sit.int221.sas.sit_announcement_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SitAnnouncementSystemBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SitAnnouncementSystemBackEndApplication.class, args);
    }


}
