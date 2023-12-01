package sit.int221.sas.sit_announcement_system_backend.service;

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
import sit.int221.sas.sit_announcement_system_backend.entity.JwtResponse;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.util.Map;

@Service
public class AzureService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    public Map<String,String> getuserAccessToken(String accessToken) throws InterruptedException {
        // fetch user info from Microsoft Graph with access token
        String graphApiUrl = "https://graph.microsoft.com/v1.0/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.exchange(graphApiUrl, HttpMethod.GET, entity, JSONObject.class);
        String userEmail = (String) response.getBody().get("mail");
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if(user == null){
                String name = (String) response.getBody().get("displayName");
                String accessTokens = jwtTokenUtil.generateVisitorAccessToken(name, userEmail);
                String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
                return Map.of("token", accessTokens, "refreshToken", refreshTokens);
        }
        String accessTokens = jwtTokenUtil.generateAccessToken(user);
        String refreshTokens = jwtTokenUtil.generateRefreshToken(accessTokens);
        return Map.of("token", accessTokens, "refreshToken", refreshTokens);
    }
}
