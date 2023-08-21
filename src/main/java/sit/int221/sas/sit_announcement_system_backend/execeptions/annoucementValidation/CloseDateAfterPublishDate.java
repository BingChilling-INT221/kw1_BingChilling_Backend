package sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CloseDateAfterPublishDateValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloseDateAfterPublishDate {
    String message() default "must be later than publish date";
    String filed() default "closeDate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

