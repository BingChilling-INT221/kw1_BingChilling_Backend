package sit.int221.sas.sit_announcement_system_backend.controller;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.Subscribe.SubscribeRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.DTO.Subscribe.SubscribeRequestToUnsubscribeDTO;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.entity.email.EmailOtpResponse;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.EmailException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.SetFiledErrorException;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.service.SubscribeService;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscribes")
//แก้ Cross origin ให้กำหนด port
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
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService ;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService ;
    @Autowired
    private AnnouncementService announcementService ;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public List<Subscribe> getSubscribeService() {
        return subscribeService.getSubscribes();
    }

    @GetMapping("/email")
    public List<Subscribe> getSubscribeByEmail(@RequestParam String email) {
        return subscribeService.getSubscribesByEmail(email);
    }
    @PostMapping("/notified_subscribe")
    public ResponseEntity<?> sendOTP(  @Valid @RequestBody SubscribeRequestDTO subscribeRequest){
        try {
            System.out.println("test notified_subscribe");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = null ;
            email = subscribeRequest.getEmail() ;
            String role = authentication.getAuthorities().stream().findFirst().get().toString() ;
            if(  email == null && (role.equals("admin") || role.equals("announcer"))){
                User user = userService.getUserByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
                email = user.getEmail();
            }
            if(email == null){ throw new EmailException("please , enter email destination due to you haven't login  ","email");}
            List<Integer> subscribesFromRequest = Arrays.stream(subscribeRequest.getSubscribes()).toList();
            System.out.println(subscribesFromRequest);
            int otp = subscribeService.sendOTP(email, "Confirm your email to Notification SAS WebApp of BingChilling Group");
            String tokenEmail = jwtTokenUtil.generateSubscribe(email, otp , subscribesFromRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new EmailOtpResponse(tokenEmail, email));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error because : "+e.getMessage());
        }
    }

    @PostMapping("/confirm_otp")
    public ResponseEntity<?> confirmOTP(HttpServletRequest request){
        try {
            String messageError = "" ;
            String token =request.getHeader("AuthorizationOtp").substring(7);
            Claims claims = (Claims) jwtTokenUtil.getClaims(token);
            List<Integer> subscribeFromClaim = (List<Integer>) claims.get("subscribe");
            try {
                subscribeService.AddSubScribe((String) claims.get("email"),subscribeFromClaim);
            }catch (Exception e){
               messageError = e.getMessage();
            }

           subscribeService.sendEmailToNotificationSubscribe((String) claims.get("email"),
                    "[ "+  claims.get("email") +" subscription @ SAS ]  Subscribed Notification ",
                    "<p> Thank you  for subscribing </p>" +
                            "<p>We are delighted to have you on board."+
                            " You will receive news from us when we announce new updates.  </p> ");
            return  messageError.length()==0?
                    ResponseEntity.status(HttpStatus.OK).body("Subscribe all successfully.") :
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("May be could Subscribed some category. but , "+ messageError);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error is : "+ e.getMessage());
        }
    }


    @DeleteMapping("/unsubscribes")
    public ResponseEntity<String>   deleteAllSubscribeByEmail(@Valid @RequestBody SubscribeRequestToUnsubscribeDTO subscribeRequest){
        try {
            List<Subscribe> subscribes = subscribeService.deleteAllSubscribe(subscribeRequest.getEmail()) ;
            System.out.println(subscribes);
            return ResponseEntity.status(HttpStatus.OK).body("Unsubscribe all successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error is : "+ e.getMessage());
        }


    }

    @DeleteMapping("/unsubscribe/id")
    public ResponseEntity<String> deleteSubscribeByIdAndByEmail(@Valid @RequestBody SubscribeRequestToUnsubscribeDTO subscribeRequest){
        try {
            List<Subscribe> subscribes = subscribeService.deleteSubscribeByEmailAndCategoryId(subscribeRequest.getEmail(),subscribeRequest.getSubscribes()) ;
            return ResponseEntity.status(HttpStatus.OK).body("You have unsubscribe "+ subscribes.stream().map(x-> x.getCategory().getCategoryName()).collect(Collectors.joining(" , ")) +" successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error is : "+ e.getMessage());
        }
    }


}
