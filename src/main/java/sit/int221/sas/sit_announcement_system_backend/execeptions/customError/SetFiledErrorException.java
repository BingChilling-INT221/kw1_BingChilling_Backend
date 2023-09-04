package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetFiledErrorException extends RuntimeException {
    private String Field;


    public SetFiledErrorException(String message, String Field) {
        super(message);
        this.Field = Field;

    }


}
