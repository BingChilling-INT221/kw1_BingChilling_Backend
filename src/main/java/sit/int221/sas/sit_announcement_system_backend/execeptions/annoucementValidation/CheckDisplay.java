package sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckDisplayValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckDisplay {
    String message() default "must be either 'Y' or 'N'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
