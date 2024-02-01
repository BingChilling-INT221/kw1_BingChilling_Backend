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
        if (roles != null && roles.isEmpty()) {
            role = roles.get(0);
        };
        String name = (String) response.get("name");
        System.out.println(userEmail + "\n");
        System.out.println(response);
        System.out.println(role + "\n");
        // fetch user info from Microsoft Graph with access token
//        String graphApiUrl = "https://graph.microsoft.com/v1.0/me";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> response = restTemplate.exchange(graphApiUrl, HttpMethod.GET, entity, JSONObject.class);
//        String userEmail = (String) response.getBody().get("mail");
        User user = null;
        try {
            user = userRepository.findByEmail(userEmail).orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (user == null) {
            String accessTokens = "";
            if ((role !=null) &&(role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("announcer"))) {

                User userObj = new User();
                userObj.setUsername(name);
                userObj.setName(name);
                userObj.setEmail(userEmail);
                userObj.setRole(Role.valueOf(role));
                String savedPassword = this.encodingArgon2.getEncryption("user.getPassword()".trim());
                userObj.setPassword(savedPassword);
                userObj = userRepository.save(userObj);
                accessTokens = jwtTokenUtil.generateAccessToken(userObj);
            } else {
                accessTokens = jwtTokenUtil.generateVisitorAccessToken(name, userEmail);
            }
            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
        if (user.getRole().toString().equals(role))
        {
            String accessTokens = jwtTokenUtil.generateAccessToken(user);
            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
        if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("announcer")) {
            user.setRole(Role.valueOf(role));
            userRepository.save(user);
            String accessTokens = jwtTokenUtil.generateAccessToken(user);
            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
        else {
            userRepository.delete(user);
            String accessTokens = jwtTokenUtil.generateVisitorAccessToken(name, userEmail);
            String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
            return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
    }
}
