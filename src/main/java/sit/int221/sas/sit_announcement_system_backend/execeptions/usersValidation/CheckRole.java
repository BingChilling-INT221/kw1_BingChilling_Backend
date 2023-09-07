package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckRoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    String message() default "please enter role specific in choice";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
