package sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sit.int221.sas.sit_announcement_system_backend.DTO.AnnouncementsRequestDTO;

public class CloseDateAfterPublishDateValidator implements ConstraintValidator<CloseDateAfterPublishDate, AnnouncementsRequestDTO> {

    @Override
    public void initialize(CloseDateAfterPublishDate constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(AnnouncementsRequestDTO announcementsRequestDTO, ConstraintValidatorContext context) {

        if(announcementsRequestDTO.getPublishDate() == null || announcementsRequestDTO.getCloseDate() == null){return true;}
        return announcementsRequestDTO.getCloseDate().isAfter(announcementsRequestDTO.getPublishDate());
    }


}
