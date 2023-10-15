package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

public class CheckRoleValidator implements ConstraintValidator<CheckRole, String> {
    @Override
    public void initialize(CheckRole constraintAnnotation) {

    }

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        for (Role roleR : Role.values()) {
            if (roleR.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
