package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCategoryByIdException extends RuntimeException {
    private String additionalField1;


    public FindCategoryByIdException(String message, String additionalField1) {
        super(message);
        this.additionalField1 = additionalField1;

    }


}
