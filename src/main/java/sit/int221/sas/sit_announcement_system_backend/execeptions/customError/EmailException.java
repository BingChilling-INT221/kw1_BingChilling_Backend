package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailException extends MessagingException {
    private String field;

    public EmailException(String message, String field) {
        super(message);
        this.field = field;

    }
}
