package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.*;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.FileException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.ForbiddenException;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.service.FileService;
import sit.int221.sas.sit_announcement_system_backend.service.SubscribeService;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("api/announcements")
@CrossOrigin(origins = {
        "http://intproj22.sit.kmutt.ac.th",
        "https://intproj22.sit.kmutt.ac.th",
        "http://localhost:5173",
        "https://localhost:5173",
        "http://ip22kw1.sit.kmutt.ac.th",
        "https://ip22kw1.sit.kmutt.ac.th",
        "http://ip22kw1.sit.kmutt.ac.th:800",
        "https://ip22kw1.sit.kmutt.ac.th:800",
})
@Validated
public class AnnouncementController<T> {

    @Autowired
    private FileService fileService ;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService ;

    @Autowired
    private SubscribeService subscribeService ;

    @GetMapping("")
    public ResponseEntity<List<AnnouncementsResponseDTO>> getAnnouncements(@RequestParam(required = false) String mode) {
//        if (mode != null && (mode.equalsIgnoreCase("active") || mode.equalsIgnoreCase("close"))) {
        return ResponseEntity.status(HttpStatus.OK).body(listMapper.mapList(announcementService.getAnnouncements(mode), AnnouncementsResponseDTO.class, modelMapper));
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(listMapper.mapList(announcementService.getAnnouncements(mode), AnnouncementsResponseDTO.class, modelMapper));
//        }

    }

   /* public ResponseEntity<List<T>> getAnnouncements(@RequestParam (required = false) String mode ) {
        if( mode != null){
            if(mode.toLowerCase().equals("active")||mode.toLowerCase().equals("close") ){
                return ResponseEntity.status(HttpStatus.OK).body((List<T>) listMapper.mapList(announcementService.getAnnouncements(mode),UserAnnouncementsResponseDTO.class, modelMapper));
            }
            throw new RuntimeException();
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body((List<T>) listMapper.mapList(announcementService.getAnnouncements(mode),AnnouncementsResponseDTO.class, modelMapper));
        }

    }*/

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementsResponseDetailDTO> getAnnouncementById(@PathVariable Integer id,
                                                                              @RequestParam(defaultValue = "false") Boolean count) {
        if (count) {
            announcementService.updateViewCount(id);
        }
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
        if (role.equals("admin")) {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.getAnnouncementById(id), AnnouncementsResponseDetailDTO.class));
        }
        System.out.println(role);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        if (username.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.getAnnouncementById(id), AnnouncementsResponseDetailDTO.class));
        }
        if (announcementService.isAuthorize(username, id)) {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.getAnnouncementById(id), AnnouncementsResponseDetailDTO.class));
        }
        throw new ForbiddenException("You are not authorized to access this resource","announcementId");
    }


    @GetMapping("/pages")
    public ResponseEntity<PageDto> getAnnouncementPage(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "5") Integer size,
                                                       @RequestParam(required = false) String mode,
                                                       @RequestParam(required = false) Integer category) {
//        if( mode != null ){
//            if(mode.toLowerCase().equals("active")||mode.toLowerCase().equals("close") ) {
//                return ResponseEntity.status(HttpStatus.OK).body(listMapper.toPageDTO(announcementService.getPages(page, size, mode, category), AnnouncementsResponseDTO.class, modelMapper));
//            }
//            else {
//                return ResponseEntity.status(HttpStatus.OK).body(listMapper.toPageDTO(announcementService.getPages(page, size, mode, category), AnnouncementsResponseDTO.class, modelMapper));
//            }
//            }
//        else {
        return ResponseEntity.status(HttpStatus.OK).body(listMapper.toPageDTO(announcementService.getPages(page, size, mode, category), AnnouncementsResponseDTO.class, modelMapper));

//        }
    }


    @PostMapping("")
    public ResponseEntity<AnnouncementsResponsehaveidDTO> createAnnouncement(@Valid @RequestBody AnnouncementsRequestDTO announcementDTO) throws MessagingException {
        announcementDTO.setOwnerName( SecurityContextHolder.getContext().getAuthentication().getName());
        Announcement  announcement = announcementService.createAnnoucement(announcementDTO);
        System.out.println(announcement.getAnnouncementOwner().getEmail());
        System.out.println(announcement.getAnnouncementCategory());
        subscribeService.sendEmailToNotificationSubscribeWhenAnnouncementUpdated(announcement);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcement, AnnouncementsResponsehaveidDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(@Role.ADMIN) or (hasAuthority(@Role.ANNOUNCER) and (@announcementService.isAuthorize(authentication.principal.username,#id)))")
    public void deleteAnnouncement(@PathVariable Integer id) throws MessagingException, FileException {
        Announcement announcement=announcementService.deleteAnnouncement(id);
        System.out.println(announcement.getAnnouncementOwner().getEmail());
        System.out.println(announcement.getAnnouncementCategory());
        subscribeService.sendEmailToNotificationSubscribeWhenAnnouncementUpdated(announcement);
        fileService.deleteFolderById(String.valueOf(announcement.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(@Role.ADMIN) or (hasAuthority(@Role.ANNOUNCER) and (@announcementService.isAuthorize(authentication.principal.username,#id)))")
    public ResponseEntity<AnnouncementsResponseDetailDTO> updateAnnouncement(@PathVariable Integer id, @Valid @RequestBody AnnouncementsRequestDTO announcmentDTO) throws MessagingException {
        Announcement announcement = announcementService.updateAnnouncement(id, announcmentDTO) ;
        System.out.println(announcement.getAnnouncementOwner().getEmail());
        System.out.println(announcement.getAnnouncementCategory());
        subscribeService.sendEmailToNotificationSubscribeWhenAnnouncementUpdated(announcement);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcement, AnnouncementsResponseDetailDTO.class));

    }

    @PutMapping("/{id}/views")
    @PreAuthorize("hasAuthority(@Role.ADMIN) or (hasAuthority(@Role.ANNOUNCER) and (@announcementService.isAuthorize(authentication.principal.username,#id)))")
    public ResponseEntity<Integer> updateAnnouncementViews(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.updateViewCount(id));
    }



//    @GetMapping("/{id}/views")
//    public ResponseEntity<Integer> getViews(@PathVariable Integer id){
//        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getViewsCount(id));

}
