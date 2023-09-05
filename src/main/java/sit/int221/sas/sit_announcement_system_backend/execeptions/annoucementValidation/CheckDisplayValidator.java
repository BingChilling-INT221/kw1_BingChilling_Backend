package sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation.CheckDisplay;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

public class CheckDisplayValidator implements ConstraintValidator<CheckDisplay, String> {

    @Override
    public void initialize(CheckDisplay constraintAnnotation) {

    }

    @Override
    public boolean isValid(String getAnnouncementDisplay, ConstraintValidatorContext context) {
        if (getAnnouncementDisplay == null) {
            return true;
        }
        return (getAnnouncementDisplay.equals(AnnouncementDisplay.N.toString())) || (getAnnouncementDisplay.equals(AnnouncementDisplay.Y.toString()));


    }
}

  /*  String errorMessage = "must be 'Y' or 'N'";

                context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(errorMessage)
                        .addPropertyNode("announcementDisplay")
                        .addConstraintViolation();
                        return false;*/