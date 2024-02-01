package sit.int221.sas.sit_announcement_system_backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AnnouncementsResponsehaveidDTO {
    private Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    //แปลง String จาก DB ได้
    @Enumerated(EnumType.STRING)
    private AnnouncementDisplay announcementDisplay;
    @JsonIgnore
    @Column(name = "announcementCategory")
    private Category category;
    private String announcementOwner;


    public Integer getCategoryId() {
        return category.getCategoryId();
    }

    public String getAnnouncementCategory() {
        return category.getCategoryName();
    }
}
