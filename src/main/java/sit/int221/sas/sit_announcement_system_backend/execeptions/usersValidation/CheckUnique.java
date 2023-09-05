package sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.mapping.UniqueKey;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CheckUniqueValidator.class)
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface CheckUnique {
    String columnName();
 //   Class<?> classname();
    String message() default "does not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
