package sit.int221.sas.sit_announcement_system_backend.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.*;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;
import sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder.SubscribeRequest;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.entity.email.EmailOtpResponse;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.ForbiddenException;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.service.SubscribeService;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
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
    public ResponseEntity<?> sendOTP( @RequestParam  (name="email",required = false) String emailRequest, @Valid @RequestBody SubscribeRequest subscribeRequest){
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = null ;
            email = emailRequest ;
            String role = authentication.getAuthorities().stream().findFirst().get().toString() ;
             if(role.equals("admin") || role.equals("announcer")){
               User   user = userService.getUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
               email = user.getEmail();

             }
                List<Integer> subscribes = Arrays.stream(subscribeRequest.getSubscribes()).toList();
            System.out.println(subscribes);

                int otp = announcementService.sendOTP(email, "Confirm your email to Notification SAS WebApp of BingChilling Group");
                String tokenEmail = jwtTokenUtil.generateSubscribe(email, otp , subscribes);
            System.out.println("sendmail");
                return ResponseEntity.status(HttpStatus.OK).body(new EmailOtpResponse(tokenEmail, email));

        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.OK).body("Error because : "+e.getMessage());
        }
    }

    @PostMapping("/confirm_otp")
    public ResponseEntity<?> confirmOTP(HttpServletRequest request){
    try {
        System.out.println("start confirm ---");
        String token =request.getHeader("AuthorizationOtp").substring(7);
        Claims claims = (Claims) jwtTokenUtil.getClaims(token);
        List<Integer> subscribe = (List<Integer>) claims.get("subscribe");
        System.out.println("start add ---");
        List<Subscribe> subscribes =subscribeService.AddSubScribe((String) claims.get("email"),subscribe);
        System.out.println("start return ---");
        return ResponseEntity.status(HttpStatus.OK).body(subscribes);
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.OK).body("Error is : "+ e.getMessage());
    }
    }

    @GetMapping("/testsub")
    public ResponseEntity<List<Subscribe>> test(){

//        System.out.println(subscribeService.getSubscribes().get(0).toString());
        return ResponseEntity.status(HttpStatus.OK).body(subscribeService.getSubscribes()) ;
    }


//    @GetMapping("/{id}/views")
//    public ResponseEntity<Integer> getViews(@PathVariable Integer id){
//        return ResponseEntity.status(HttpStatus.OK).body(announcementService.getViewsCount(id));

}
