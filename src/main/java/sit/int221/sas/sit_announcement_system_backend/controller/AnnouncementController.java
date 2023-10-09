package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.*;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
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
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AnnouncementService announcementService;

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
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.getAnnouncementById(id), AnnouncementsResponseDetailDTO.class));
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
    public ResponseEntity<AnnouncementsResponsehaveidDTO> createAnnouncement(@Valid @RequestBody AnnouncementsRequestDTO announcementDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.createAnnoucement(announcementDTO), AnnouncementsResponsehaveidDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(@Role.ADMIN) or (hasAuthority(@Role.ANNOUNCER) and (@announcementService.isAuthorize(authentication.principal.username,#id)))")
    public void deleteAnnouncement(@PathVariable Integer id) {
        announcementService.deleteAnnouncement(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(@Role.ADMIN) or (hasAuthority(@Role.ANNOUNCER) and (@announcementService.isAuthorize(authentication.principal.username,#id)))")
    public ResponseEntity<AnnouncementsResponseDetailDTO> updateAnnouncement(@PathVariable Integer id, @Valid @RequestBody AnnouncementsRequestDTO announcmentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(announcementService.updateAnnouncement(id, announcmentDTO), AnnouncementsResponseDetailDTO.class));

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
