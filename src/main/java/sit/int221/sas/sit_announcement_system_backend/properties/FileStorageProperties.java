package sit.int221.sas.sit_announcement_system_backend.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix =  "file")
@Getter
@Setter

public class FileStorageProperties {
    private String uploadDir;
}
