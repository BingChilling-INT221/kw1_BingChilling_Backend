package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationErrorException extends RuntimeException {
    public AuthenticationErrorException(String message) {
        super(message);

    }
}
