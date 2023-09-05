package sit.int221.sas.sit_announcement_system_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor

@Table(name = "announcement")
public class Announcement {
    //อาจจะทำให้ไอดี nullable = true ก็ใช้ได้
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "announcementTitle", nullable = false, length = 200)
    private String announcementTitle;
    @Column(name = "announcementDescription", nullable = false, length = 10000)

    private String announcementDescription;


    @ManyToOne
    @JoinColumn(name = "announcementCategory", nullable = false)
    private Category announcementCategory;
//    public String getAnnouncementCategory(){

    //        return category.getCategoryName();
//    }
    @Column(name = "publishDate", nullable = true)
    private ZonedDateTime publishDate;
    @Column(name = "closeDate", nullable = true)
    private ZonedDateTime closeDate;
    //แปลง String จาก DB ได้

    @Column(name = "announcementDisplay", nullable = true)
    @Enumerated(EnumType.STRING)
    private AnnouncementDisplay announcementDisplay;

    public Announcement() {
        this.announcementDisplay = AnnouncementDisplay.N;
    }

    @Column(name = "viewCount", insertable = false)
    private Integer viewCount;

}
