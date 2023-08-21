package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckTrimValidator implements ConstraintValidator<CheckTrim, String> {
    @Override
    public void initialize(CheckTrim constraintAnnotation) {

    }
    @Override
    public boolean isValid(String filed, ConstraintValidatorContext context) {
        return filed.trim().length() == filed.length();


    }
}
