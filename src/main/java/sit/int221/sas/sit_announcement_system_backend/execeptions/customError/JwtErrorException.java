package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;

import javax.naming.AuthenticationException;

@Getter
@Setter
public class JwtErrorException extends RuntimeException {
        private String field;

    public JwtErrorException(String message, String field) {
        super(message);
        this.field = field;

    }
}
