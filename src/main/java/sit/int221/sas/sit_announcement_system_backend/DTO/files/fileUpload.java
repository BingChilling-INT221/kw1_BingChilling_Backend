package sit.int221.sas.sit_announcement_system_backend.DTO.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class fileUpload implements Serializable {
    private MultipartFile file;
}
