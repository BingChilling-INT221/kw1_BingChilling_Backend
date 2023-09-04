package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationErrorException extends RuntimeException {
    private String field ;
    public AuthenticationErrorException(String message,String field) {
        super(message);
        this.field = field ;

    }
}
