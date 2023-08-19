package sit.int221.sas.sit_announcement_system_backend.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
//แก้ Cross origin ให้กำหนด port
@CrossOrigin(origins = "http://localhost:80")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper ;
    @GetMapping("")
    public List<User> getUsers(){
        return  userService.getAllUser();
    }

    @GetMapping("/{userid}")
    public User getUserDetail(@PathVariable Integer userid){
        return  userService.getDetailUser(userid) ;
    }

    @PostMapping("")
    public  User creatUser(@RequestBody UserRequestDTO userObj){
    //ถามเรื่อง db ควร notnull ไหม
        return userService.creatUser(userObj);
    }

    @PutMapping("/{userid}")
    public  User updateUser(@PathVariable Integer userid , @RequestBody UserRequestDTO userObj){
        return userService.updateUser(userid,userObj);
    }



}
