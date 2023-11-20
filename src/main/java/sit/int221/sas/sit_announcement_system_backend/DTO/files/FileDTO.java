package sit.int221.sas.sit_announcement_system_backend.DTO.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class FileDTO implements Serializable {
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long size;
    public void nullFile(){
        this.fileUrl = null;
    }
}
