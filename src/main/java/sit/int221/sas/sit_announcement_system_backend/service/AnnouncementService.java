package sit.int221.sas.sit_announcement_system_backend.service;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.sas.sit_announcement_system_backend.DTO.AnnouncementsRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.SetFiledErrorException;
import sit.int221.sas.sit_announcement_system_backend.properties.MailProperties;
import sit.int221.sas.sit_announcement_system_backend.repository.AnnouncementRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.CategoryRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.SubscribeRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

@Service
public class AnnouncementService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private SubscribeRepository subscribeRepository ;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private MailProperties mailProperties ;

//    private List<Announcement> getAnnouncementsByOwner(String ownerId) {
//
//        return announcementRepository.findByAnnouncementOwnerOrderByCloseDateDesc(ownerId);
//    }

    public List<Announcement> updateAnnouncementsByAnnouncementOwner(String ownerId, String newOwner) {
        List<Announcement> announcements = announcementRepository.findByAnnouncementOwnerOrderByCloseDateDesc(ownerId);
        announcements.forEach(announcement -> announcement.setAnnouncementOwner(newOwner));
        return announcementRepository.saveAllAndFlush(announcements);
    }

    public List<Announcement> getAnnouncementsByOwnerName(String ownerName) {
        return announcementRepository.findByAnnouncementOwnerOrderByCloseDateDesc(ownerName);
    }

    public boolean isAuthorize(String username, Integer announcementId) {
        System.out.println(username + " " + announcementId);
        Announcement announcement = announcementRepository.findById(announcementId).orElse(null);
//        System.out.println(announcement.getId());
//        System.out.println(announcement.getAnnouncementOwner().getUsername());
        return announcement != null && announcement.getAnnouncementOwner().equals(username);
    }

    public List<Announcement> getAnnouncements(String mode, Integer category) {
        Authentication payload = SecurityContextHolder.getContext().getAuthentication();

        String role = payload.getAuthorities().stream().findFirst().get().getAuthority();
        String username = payload.getName();
//        System.out.println(payload);
//        System.out.println("--------------------");
//        System.out.println(role);
        if (role.equalsIgnoreCase("announcer")) {
//            System.out.println("announcer");
            return announcementRepository.findByAnnouncementOwnerOrderByCloseDateDesc(username);
        } else if (role.equalsIgnoreCase("admin")) {
//            System.out.println("admin");
            return announcementRepository.findAllByOrderByIdDesc();
        } else {
            LocalDateTime localNow = LocalDateTime.now();
            if (mode != null) {
                if (mode.equalsIgnoreCase("active")) {
//                    System.out.println("active");
                    return announcementRepository.findAnnouncementByValidateDatetimeList(localNow.atZone(ZoneId.of("UTC")));
                } else if (mode.equalsIgnoreCase("close")) {
//                    System.out.println("close");
                    return announcementRepository.findAnnouncementByCloseDateAfterNowList(localNow.atZone(ZoneId.of("UTC")));
                } else {
//                    System.out.println("not found");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found : " + mode + " mode .");
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found : ");
        }


    }


    public Announcement getAnnouncementById(Integer announcementid) {

        return announcementRepository.findById(announcementid).orElseThrow(() -> new NotfoundByfield(("Announcement id " + announcementid + " does not exist"), "id"));
//        announcement.setViewCount(announcement.getViewCount()+1);
//        announcementRepository.saveAndFlush(announcement);
//        return announcement ;
    }

    public Announcement createAnnoucement(AnnouncementsRequestDTO announcementDTO) {
        Announcement announcement = new Announcement();
        return getAnnouncement(announcementDTO, announcement);
    }

    public Announcement deleteAnnouncement(Integer id) {
        Announcement announcement=announcementRepository.findById(id).orElseThrow(() -> new NotfoundByfield(("Announcement id " + id + " does not exist"), "id"));
        announcementRepository.deleteById(id);
        return announcement ;
    }


    public Announcement updateAnnouncement(Integer id, AnnouncementsRequestDTO announcement) {
        try {
            Announcement existAnnouncement = announcementRepository.findById(id).orElseThrow(
                    () -> new NotfoundByfield(("Announcement id " + id + " does not exist"), "id"));
            announcement.setOwnerName(existAnnouncement.getAnnouncementOwner());
            return getAnnouncement(announcement, existAnnouncement);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


    }

    private Announcement getAnnouncement(AnnouncementsRequestDTO announcement, Announcement RealAnnouncement) {
        RealAnnouncement.setAnnouncementTitle(announcement.getAnnouncementTitle());
        RealAnnouncement.setAnnouncementDescription(announcement.getAnnouncementDescription());
        RealAnnouncement.setPublishDate(announcement.getPublishDate());
        RealAnnouncement.setCloseDate(announcement.getCloseDate());
        if (announcement.getAnnouncementDisplay() != null) {
            RealAnnouncement.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        }
        RealAnnouncement.setAnnouncementOwner(announcement.getOwnerName());
        RealAnnouncement.setAnnouncementCategory(categoryRepository.findById(announcement.getCategoryId()).orElseThrow(() -> new SetFiledErrorException("does not exists", "categoryId")));
        return announcementRepository.saveAndFlush(RealAnnouncement);
    }


    public Page<Announcement> getPages(Integer page, Integer size, String mode, Integer category) {
//        page= page!=null?page:0 ;
//        size=size!=null?size:size(getAnnouncements(null));
        Pageable pageable = PageRequest.of(page, size);
        Authentication payload = SecurityContextHolder.getContext().getAuthentication();
        String role = payload.getAuthorities().stream().findFirst().get().getAuthority();
        String username = payload.getName();
//        System.out.println(payload);
//        System.out.println("--------------------");
//        System.out.println(role);
        if (role.equalsIgnoreCase("announcer")) {
            System.out.println("announcer");
            if (category == null) {
                return announcementRepository.findByAnnouncementOwnerOrderByCloseDateDesc(username, pageable);
            } else {
                return announcementRepository.findAnnouncementByAnnouncementCategory_CategoryIdOrderByIdDesc(category, pageable);
            }
        } else if (role.equalsIgnoreCase("admin")) {
            System.out.println("admin");
            if (category == null) {
                return announcementRepository.findAllByOrderByIdDesc(pageable);
            } else {
                return announcementRepository.findAnnouncementByAnnouncementCategory_CategoryIdOrderByIdDesc(category, pageable);
            }
        }
        LocalDateTime localNow = LocalDateTime.now();
        if (category != null && mode != null) {
            if (mode.equalsIgnoreCase("active")) {
                return announcementRepository.findAnnouncementByValidateDatetimePageWithId(localNow.atZone(ZoneId.of("UTC")), category, pageable);
            } else if (mode.equalsIgnoreCase("close")) {
                return announcementRepository.findAnnouncementByCloseDateAfterNowPageWithId(localNow.atZone(ZoneId.of("UTC")), category, pageable);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found : " + mode + "mode .");
            }
        } else if (category != null) {
            return announcementRepository.findAnnouncementByAnnouncementCategory_CategoryIdOrderByIdDesc(category, pageable);
        } else if (mode != null) {
            if (mode.equalsIgnoreCase("active")) {
                return announcementRepository.findAnnouncementByValidateDatetimePage(localNow.atZone(ZoneId.of("UTC")), pageable);
            } else if (mode.equalsIgnoreCase("close")) {
                return announcementRepository.findAnnouncementByCloseDateAfterNowPage(localNow.atZone(ZoneId.of("UTC")), pageable);
            } else {
                throw new SetFiledErrorException("Not Found : " + mode + "mode .", "categoryId");
            }
        } else {
            return announcementRepository.findAllByOrderByIdDesc(pageable);
        }

    }

    public Integer updateViewCount(Integer id) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new SetFiledErrorException("does not exists", "announcementId"));
        announcement.setViewCount(announcement.getViewCount() + 1);
        announcementRepository.saveAndFlush(announcement);
        return announcement.getViewCount();
    }

  /*  public Integer getViewsCount(Integer id){
        return announcementRepository.viewcountById(id);
    }*/
}


// if(mode != null){
//         if(mode.toLowerCase().equals("active") || mode.toLowerCase().equals("close")){
//         List<Announcement> announcements =    announcementRepository.findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay.Y);
//        Iterator<Announcement> iterator = announcements.iterator();
//        if(mode.toLowerCase().equals("active")) {
//        while (iterator.hasNext()) {
//        Announcement announcement = iterator.next();
//        if (announcement.getCloseDate() != null) {
//        if (Instant.now().isAfter(announcement.getCloseDate().toInstant())) {
//        iterator.remove();
//        }
//        }
//        if (announcement.getPublishDate() != null) {
//        if (Instant.now().isBefore(announcement.getPublishDate().toInstant())) {
//        iterator.remove();
//        }
//        }
//        }
//        }
//        else {
//        while (iterator.hasNext()) {
//        Announcement announcement = iterator.next();
//        if(announcement.getCloseDate() == null){
//        iterator.remove();
//        }
//        if (announcement.getCloseDate() != null &&!Instant.now().isAfter(announcement.getCloseDate().toInstant())) {
//        iterator.remove();
//        }
//        }
//        }
//        return announcements;
//        }
//        else {
//        return announcementRepository.findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay.N);
//        }
//        }
//        else {
//        return announcementRepository.findAllByOrderByIdDesc();
//        }