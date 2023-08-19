package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotfoundById extends RuntimeException  {
    private String field ;
    public NotfoundById(String message, String additionalField1) {
        super(message);
        this.field= additionalField1;

    }
}
