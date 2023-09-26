package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTErrorException extends RuntimeException{
    private String field;
    public JWTErrorException(String message, String field) {
        super(message);
        this.field= field;

    }
}
