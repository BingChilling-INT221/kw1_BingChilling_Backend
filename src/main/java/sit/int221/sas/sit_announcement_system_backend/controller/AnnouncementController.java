package sit.int221.sas.sit_announcement_system_backend.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.*;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.config.JwtUserDetailsService;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtRequest;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe.SubscribeRequest;
import sit.int221.sas.sit_announcement_system_backend.entity.email.EmailOtpResponse;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.ForbiddenException;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.Arrays;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

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
    public ResponseEntity<AnnouncementsResponsehaveidDTO> createAnnouncement(@Valid @RequestBody AnnouncementsRequestDTO announcementDTO) {
        announcementDTO.setOwnerName( SecurityContextHolder.getContext().getAuthentication().getName());
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



    @PostMapping("/notified_subscribe")
    public ResponseEntity<?> sendOTP( @RequestParam ("email") String email, @Valid @RequestBody SubscribeRequest subscribeRequest){
        try {
            String [] subscribes = subscribeRequest.getSubscribes();
            System.out.println(Arrays.toString(subscribes));
            int otp=announcementService.sendOTP(email,"Confirm your email to Login SAS WebApp of BingChilling Group");
            String tokenEmail=jwtTokenUtil.generateSubscribe(email,otp);
            return ResponseEntity.status(HttpStatus.OK).body(new EmailOtpResponse(tokenEmail,email));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body("Error");
        }
    }

    @PostMapping("/confirm_otp")
    public ResponseEntity<String> confirmOTP(){
    try {
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.OK).body("Error");
    }
    }



//    @GetMapping("/{id}/views")
//    public ResponseEntity<Integer> getViews(@PathVariable Integer id){
//        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getViewsCount(id));

}
