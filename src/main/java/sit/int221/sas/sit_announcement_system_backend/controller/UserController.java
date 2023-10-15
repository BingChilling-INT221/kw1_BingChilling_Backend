package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.*;
import sit.int221.sas.sit_announcement_system_backend.config.JwtTokenUtil;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.ForbiddenException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;
import sit.int221.sas.sit_announcement_system_backend.service.AnnouncementService;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public List<UserResponseDTO> getUsers() {
        return listMapper.mapList(userService.getAllUser(), UserResponseDTO.class, modelMapper);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserDetail(@PathVariable Integer id) {
        return modelMapper.map(userService.getDetailUser(id), UserResponseDTO.class);
    }

    @PostMapping("")
    public UserResponseDTO createUser(@Valid @RequestBody UseRequestRegisterDTO userObj) throws InterruptedException {
        return modelMapper.map(userService.createUser(userObj), UserResponseDTO.class);
    }
    @GetMapping("/announcer")
    public UserResponseDTO getSelfAnnouncer(@RequestHeader(name = "Authorization") String header) {
        if(!header.startsWith("Bearer ")){ throw new JwtErrorException("Bearer error.","token"); }
        String token = header.substring(7);
        String username = jwtTokenUtil.getSubjectFromToken(token);
        System.out.println(username);
        return modelMapper.map(userService.getUserByUsername(username), UserResponseDTO.class);
    }
    @PostMapping("/announcer")
    public UserResponseDTO createUserAnnouncer(@Valid @RequestBody UserAnnouncerRequestRegisterDTO userObj) throws InterruptedException {
        userObj.setRole("announcer");
        UseRequestRegisterDTO user = modelMapper.map(userObj, UseRequestRegisterDTO.class);
        return modelMapper.map(userService.createUser(user), UserResponseDTO.class);
    }
    @PutMapping("/announcer")
    public UserResponseDTO updateUserAnnouncer(@RequestHeader(name = "Authorization") String header, @Valid @RequestBody UserUpdateRequestDTO userObj) throws InterruptedException {
        if(!header.startsWith("Bearer ")){ throw new JwtErrorException("Bearer error.","token"); }
        String token = header.substring(7);
        String username = jwtTokenUtil.getSubjectFromToken(token);
        userObj.setRole("announcer");
        return modelMapper.map(userService.updateUser(userService.getUserByUsername(username).getId(), userObj), UserResponseDTO.class);
    }
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequestDTO userObj) throws InterruptedException {
        return modelMapper.map(userService.updateUser(id, userObj), UserResponseDTO.class);
    }

    @DeleteMapping("/{id}")

    public void delete(@RequestHeader(name = "Authorization") String header, @PathVariable Integer id) {
        if(!header.startsWith("Bearer ")){ throw new JwtErrorException("Bearer error.","token"); }
        String token = header.substring(7);
        String username = jwtTokenUtil.getSubjectFromToken(token);
        if(username.equals(userService.getUserById(id).getUsername())){
            throw new ForbiddenException("You can't delete yourself.","error");
        }
        User newOwner = userService.getUserByUsername(username);
        announcementService.updateAnnouncementsByAnnouncementOwner(id,newOwner);
        userService.deleteUser(id);

    }

    @PostMapping("/match")
    public void matchPassword(@Valid @RequestBody UserLoginRequestDTO userLogin) {
        userService.matchPassword(userLogin.getUsername(), userLogin.getPassword());
    }



}
