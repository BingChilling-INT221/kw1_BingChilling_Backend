package sit.int221.sas.sit_announcement_system_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.sas.sit_announcement_system_backend.DTO.AnnouncementsRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.DTO.AnnouncementsResponseDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.repository.AnnouncementRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.CategoryRepository;
import sit.int221.sas.sit_announcement_system_backend.repository.PagingAnnouncementRepository;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AnnouncementService {
    @Autowired
    private PagingAnnouncementRepository pagingAnnouncementRepository ;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Announcement> getAnnouncements(String mode) {

        if(mode != null){
            if(mode.toLowerCase().equals("active") || mode.toLowerCase().equals("close")){
                List<Announcement> announcements =    announcementRepository.findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay.Y);
                Iterator<Announcement> iterator = announcements.iterator();
                if(mode.toLowerCase().equals("active")) {
                    while (iterator.hasNext()) {
                            Announcement announcement = iterator.next();
                            if (announcement.getCloseDate() != null) {
                                if (Instant.now().isAfter(announcement.getCloseDate().toInstant())) {
                                    iterator.remove();
                                }
                            }
                            if (announcement.getPublishDate() != null) {
                                if (Instant.now().isBefore(announcement.getPublishDate().toInstant())) {
                                    iterator.remove();
                                    }
                                }
                            }
                        }
                    else {
                    while (iterator.hasNext()) {
                            Announcement announcement = iterator.next();
                            if(announcement.getCloseDate() == null){
                                iterator.remove();
                            }
                            if (announcement.getCloseDate() != null &&!Instant.now().isAfter(announcement.getCloseDate().toInstant())) {
                                iterator.remove();
                            }
                        }
                    }
                return announcements;
            }
            else {
                return announcementRepository.findByAnnouncementDisplayOrderByIdDesc(AnnouncementDisplay.N);
            }
        }
        else {
            return announcementRepository.findAllByOrderByIdDesc();
        }
    }


    public Announcement getAnnouncementById(Integer announcementid) {
        return announcementRepository.findById(announcementid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Announcement id"+announcementid+ " does not exist"));
    }
    public Announcement createAnnoucement(AnnouncementsRequestDTO announcementDTO){
        Announcement announcement = new Announcement();
        return getAnnouncement(announcementDTO, announcement);
    }

    public void deleteAnnouncement(Integer id){
        announcementRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND," Announcement id" +id + " does not exist, Can not delete !")) ;
        announcementRepository.deleteById(id);
    }


    public Announcement updateAnnouncement(Integer id, AnnouncementsRequestDTO announcement) {
        try {
            Announcement existAnnouncement = announcementRepository.findById(id).orElseThrow(
                    ()->new   ResponseStatusException(HttpStatus.NOT_FOUND,"Announcement id" +id + " does not exist, Can not update !"));
            return    getAnnouncement(announcement, existAnnouncement);
        }
        catch (RuntimeException e) {
         throw new RuntimeException(e);
        }


    }

    private Announcement getAnnouncement(AnnouncementsRequestDTO announcement, Announcement RealAnnouncement) {
        RealAnnouncement.setAnnouncementTitle(announcement.getAnnouncementTitle());
        RealAnnouncement.setAnnouncementDescription(announcement.getAnnouncementDescription());
        RealAnnouncement.setPublishDate(announcement.getPublishDate());
        RealAnnouncement.setCloseDate(announcement.getCloseDate());
        if(announcement.getAnnouncementDisplay()!=null){
            RealAnnouncement.setAnnouncementDisplay(announcement.getAnnouncementDisplay());
        }
        RealAnnouncement.setAnnouncementCategory(categoryRepository.findById(announcement.getCategoryId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category id" +announcement.getCategoryId()+" does not exist, Can not update !")));
        return  announcementRepository.saveAndFlush(RealAnnouncement);
    }


    public Page<Announcement> getPages(Integer page, Integer pageSize,String mode,Integer id){
        Pageable pageable = PageRequest.of(page,pageSize);
        LocalDateTime localNow = LocalDateTime.now();
        if( id != null && mode != null){

            if(mode.toLowerCase().equals("active")){
                return announcementRepository.findAnnouncementByValidateDatetimePageWithId(localNow.atZone(ZoneId.of("UTC")),id,pageable);
            }
            else {
                return announcementRepository.findAnnouncementByCloseDateAfterNowPageWithId(localNow.atZone(ZoneId.of("UTC")),id,pageable) ;
            }

        }
       else if(id != null && mode==null) {
            return announcementRepository.findAnnouncementByAnnouncementCategory_CategoryIdOrderByIdDesc(id,pageable) ;
        }
       else if(id==null && mode!=null){
           if(mode.toLowerCase().equals("active")){
              return  announcementRepository.findAnnouncementByValidateDatetimePage(localNow.atZone(ZoneId.of("UTC")),pageable);
           }
           else {
               return  announcementRepository.findAnnouncementByCloseDateAfterNowPage(localNow.atZone(ZoneId.of("UTC")),pageable);
           }

        }
        else{
            return announcementRepository.findAll(pageable);
        }

    }

}


