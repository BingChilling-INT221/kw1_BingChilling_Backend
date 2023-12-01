package sit.int221.sas.sit_announcement_system_backend.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/ms")
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
public class MSAuthenticationController {

    @GetMapping("")
    public String getMsAuthentication(Model model, Authentication user) {
        System.out.println(model.getBodyText());
        System.out.println(model.toString());
        System.out.println(user.getName());
        System.out.println(user.getAuthorities());
        return "Successful for Authentication";
    }
    @GetMapping(path = "/some_path")
    public void tokenDetails(@AuthenticationPrincipal OidcUser principal) {
        Map<String, Object> claims = principal.getIdToken().getClaims();
        System.out.println(claims==null);
    }
    @GetMapping("/test")
    public String test() {
        return "Successful for Authentication";
    }

    @PostMapping("/testpost")
    public String testPost() {
        return "Successful for Authentication";
    }

}
