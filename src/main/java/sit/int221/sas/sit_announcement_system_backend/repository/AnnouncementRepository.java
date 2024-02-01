package sit.int221.sas.sit_announcement_system_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {


    @Query(value = "SELECT a.viewCount FROM Announcement a WHERE a.id = :id")
    Integer viewcountById(Integer id);

    //ธรรมดา
    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y' AND ((a.publishDate IS NULL OR :now >= a.publishDate) AND (a.closeDate IS NULL OR :now < a.closeDate)) ORDER BY a.id DESC ")
    List<Announcement> findAnnouncementByValidateDatetimeList(@Param("now") ZonedDateTime now);


    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y' AND ( :now >= a.closeDate) ORDER BY a.id DESC ")
    List<Announcement> findAnnouncementByCloseDateAfterNowList(@Param("now") ZonedDateTime now);
    //PAGE

    //ไม่มี ID
    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y'AND ((a.publishDate IS NULL OR  :now >= a.publishDate) AND ( a.closeDate IS NULL OR :now < a.closeDate )) ORDER BY a.id DESC ")
    Page<Announcement> findAnnouncementByValidateDatetimePage(@Param("now") ZonedDateTime now, Pageable pageable);

    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y'AND ( :now >= a.closeDate) ORDER BY a.id DESC ")
    Page<Announcement> findAnnouncementByCloseDateAfterNowPage(@Param("now") ZonedDateTime now, Pageable pageable);


    // มี ID
    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y'AND ( ((a.publishDate IS NULL OR :now >= a.publishDate) AND (a.closeDate IS NULL OR :now < a.closeDate)) AND a.announcementCategory.categoryId = :id ) ORDER BY a.id DESC ")
    Page<Announcement> findAnnouncementByValidateDatetimePageWithId(@Param("now") ZonedDateTime now, @Param("id") Integer id, Pageable pageable);


    @Query(value = "SELECT a FROM Announcement a WHERE  a.announcementDisplay='Y'AND (   :now >= a.closeDate AND a.announcementCategory.categoryId = :id) ORDER BY a.id DESC ")
    Page<Announcement> findAnnouncementByCloseDateAfterNowPageWithId(@Param("now") ZonedDateTime now,
                                                                     Integer id,
                                                                     Pageable pageable);


    List<Announcement> findAllByOrderByIdDesc();

    Page<Announcement> findAllByOrderByIdDesc(Pageable page);


    List<Announcement> findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay Display); //เปิด

    Page<Announcement> findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay Display, Pageable pageable); // ไม่เปิด // user ดูไม่ได้

    Page<Announcement> findAnnouncementByAnnouncementCategory_CategoryIdOrderByIdDesc(Integer id, Pageable pageable);

    List<Announcement> findByAnnouncementOwnerOrderByCloseDateDesc(String username);
    Page<Announcement> findByAnnouncementOwnerOrderByCloseDateDesc(String username, Pageable pageable);

}
