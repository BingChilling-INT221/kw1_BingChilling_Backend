package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckTrimValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckTrim {
    String message() default "must be trim before'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
