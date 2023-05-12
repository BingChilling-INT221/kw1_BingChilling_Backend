package sit.int221.sas.sit_announcement_system_backend.DTO;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.CheckDisplay;
import sit.int221.sas.sit_announcement_system_backend.execeptions.CloseDateAfterPublishDate;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Getter
@Setter
@CloseDateAfterPublishDate

public class AnnouncementsRequestDTO {
    @Column(name = "announcementTitle", nullable = false)
    @NotNull(message = "can not be null")
    @Size(max = 200)
    private String announcementTitle;
    @Column(name = "announcementDescription", nullable = false)
    @NotNull(message = "can not be null")
    @Size(max = 10000)
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
    @NotNull(message = "can not be null")
    @CheckDisplay
    private String announcementDisplay;
    public AnnouncementDisplay getAnnouncementDisplay() {
        return AnnouncementDisplay.valueOf(announcementDisplay);
    }
    @Column(name = "announcementCategory", nullable = false)
    @NotNull(message = "can not be null")
    private Integer categoryId;
}
