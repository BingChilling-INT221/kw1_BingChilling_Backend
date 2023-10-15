package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameException extends RuntimeException {
    private String field;

    public UsernameException(String message, String field) {
        super(message);
        this.field = field;

    }
}
