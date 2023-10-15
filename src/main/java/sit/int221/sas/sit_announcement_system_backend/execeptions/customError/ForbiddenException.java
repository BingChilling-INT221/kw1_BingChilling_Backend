package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private String field;
    public ForbiddenException(String message, String additionalField1) {
        super(message);
        this.field = additionalField1;
    }
}