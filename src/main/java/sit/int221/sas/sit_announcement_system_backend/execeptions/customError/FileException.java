package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileException extends IOException {
    private String field;

    public FileException(String message, String field) {
       super(message);
        this.field = field;

    }
}
