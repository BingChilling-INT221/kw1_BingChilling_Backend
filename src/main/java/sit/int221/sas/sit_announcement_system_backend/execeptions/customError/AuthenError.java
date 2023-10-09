package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

@Getter
@Setter
public class AuthenError extends AuthenticationException {
    private String field;

    public AuthenError(String message, String field) {
        super(message);
        this.field = field;

    }
}
