package sit.int221.sas.sit_announcement_system_backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UseRequestRegisterDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.JwtResponse;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.service.AzureService;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;
import sit.int221.sas.sit_announcement_system_backend.utils.TokenPayload;

import java.util.Map;

@RestController
@RequestMapping("api/azure")
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
public class AzureController {
    @Autowired
    private AzureService azureService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/token")
    // รับ access token จาก Microsoft Graph API และส่ง Token กลับไปให้ Frontend
    // ใช้ TokenPaylaod เพราะว่าต้องการแค่ token ไม่ต้องการข้อมูลอื่นๆ และ token ใหญ่มาก
    public ResponseEntity<?> getToken(@RequestBody TokenPayload token) {
//        System.out.println("token: " + token);
        String accessToken = token.getToken();
        // ใช้ access token เพื่อเรียกข้อมูลจาก Microsoft Graph API ต่อไป
        try {
            Map<String, String> tokens = azureService.getuserAccessToken(accessToken);
            System.out.println("accessToken: " + tokens.get("accessToken"));
            System.out.println("refreshToken: " + tokens.get("refreshToken"));
            return ResponseEntity.ok(tokens);
        }
       catch (NotfoundByfield e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
//    public String getUserEmailFromGraphApi(String accessToken) {
//        String graphApiUrl = "https://graph.microsoft.com/v1.0/me";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> response = restTemplate.exchange(graphApiUrl, HttpMethod.GET, entity, JSONObject.class);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            // ประมวลผลข้อมูลที่ได้จาก Microsoft Graph API
//            System.out.println("response: " + response.getBody().toString());
//            System.out.println("response: " + response.getBody().get("mail"));
//            return (String) response.getBody().get("mail");
//        } else {
//            return null;
//        }
//    }

}
