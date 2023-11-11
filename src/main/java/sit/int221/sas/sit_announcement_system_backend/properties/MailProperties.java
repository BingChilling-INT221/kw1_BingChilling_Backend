package sit.int221.sas.sit_announcement_system_backend.properties;//package sit.int204.classicmodels.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter

public class MailProperties {
    private String username;
    private String password;
}
