package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailException extends MessagingException {
    private String field;

    public EmailException(String message, String field) {
        super(message);
        this.field = field;

    }
}
