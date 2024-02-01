package sit.int221.sas.sit_announcement_system_backend.service;

import io.jsonwebtoken.Claims;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UseRequestRegisterDTO;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.config.JwtValidator;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtResponse;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;
import sit.int221.sas.sit_announcement_system_backend.utils.Argon2Class;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import java.util.ArrayList;
import java.util.Map;

@Service
public class AzureService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private Argon2Class encodingArgon2;
    @Autowired
    private UserService userService;
    public Map<String, String> getuserAccessToken(String accessToken) throws InterruptedException {
//        System.out.println(accessToken);

        Claims response = jwtValidator.decodeAndVerifyToken(accessToken);
        String userEmail = (String) response.get("preferred_username");
        ArrayList<String> roles = (ArrayList<String>) response.get("roles");
        String role = null;
        if (roles != null && !roles.isEmpty()) {
            role = roles.get(0);
        };
        String name = (String) response.get("name");
        User user = null;
        try {
            user = userRepository.findByEmail(userEmail).orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (user == null) {
            String accessTokens = "";
            if ((role !=null) &&(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("announcer"))) {
                String username = userEmail.split("@")[0];
                accessTokens = jwtTokenUtil.generateAzureAccessToken(username, userEmail, Role.valueOf(role).toString(),accessToken);
            } else {
                accessTokens = jwtTokenUtil.generateVisitorAccessToken(name, userEmail);
            }
            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
        accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshTokens = jwtTokenUtil.generateRefreshToken(accessToken);
        return Map.of("token", accessToken, "refreshToken", refreshTokens);
//        if (user.getRole().toString().equals(role))
//        {
//            String accessTokens = jwtTokenUtil.generateAccessToken(user);
//            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
//            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
//        }
//        if (role == null) {
//            role = "";
//        }
//        if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("announcer")) {
//            user.setRole(Role.valueOf(role));
//            userRepository.saveAndFlush(user);
//            String accessTokens = jwtTokenUtil.generateAccessToken(user);
//            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
//            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
//        }
//        else {
//            userRepository.delete(user);
//            String accessTokens = jwtTokenUtil.generateVisitorAccessToken(name, userEmail);
//            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
//            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
//        }
    }
}
