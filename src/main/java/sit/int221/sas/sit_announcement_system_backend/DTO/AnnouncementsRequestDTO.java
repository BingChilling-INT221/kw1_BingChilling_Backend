package sit.int221.sas.sit_announcement_system_backend.DTO;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation.CheckDisplay;
import sit.int221.sas.sit_announcement_system_backend.execeptions.annoucementValidation.CloseDateAfterPublishDate;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Getter
@Setter
@CloseDateAfterPublishDate

public class AnnouncementsRequestDTO {
    @Column(name = "announcementTitle", nullable = false)

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 200)
    private String announcementTitle;
    @Column(name = "announcementDescription", nullable = false)

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 10000)
    private String announcementDescription;
    @Column(name = "publishDate", nullable = true)
    @FutureOrPresent
    private ZonedDateTime publishDate;
    @Column(name = "closeDate", nullable = true)
    @Future
    private ZonedDateTime closeDate;
    //แปลง String จาก DB ได้

    @Column(name = "announcementDisplay", nullable = true)
    @Enumerated(EnumType.STRING)
    @CheckDisplay
    private String announcementDisplay;

    public AnnouncementDisplay getAnnouncementDisplay() {
        if (announcementDisplay != null) {
            return AnnouncementDisplay.valueOf(announcementDisplay);
        } else {
            return AnnouncementDisplay.N;
        }
    }

    @Column(name = "announcementCategory", nullable = false)
    @NotNull(message = "must not be null")
    private Integer categoryId;

    @Column(name = "announcementOwner", nullable = false)
    private String ownerName;
}
