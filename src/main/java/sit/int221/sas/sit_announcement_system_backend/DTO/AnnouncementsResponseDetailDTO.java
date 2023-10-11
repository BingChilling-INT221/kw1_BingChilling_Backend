package sit.int221.sas.sit_announcement_system_backend.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;
import java.time.ZonedDateTime;

@Getter
@Setter
public class AnnouncementsResponseDetailDTO {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    @Enumerated(EnumType.STRING)
    private AnnouncementDisplay announcementDisplay;
    private String announcementCategory;
    private Integer viewCount;
    private User announcementOwner;

    public String getAnnouncementOwner(){
        return announcementOwner.getUsername();
    }
}
