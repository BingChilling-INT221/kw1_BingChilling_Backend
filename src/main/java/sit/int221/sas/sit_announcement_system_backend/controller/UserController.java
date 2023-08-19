package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.validation.Valid;
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
@CrossOrigin(origins = {
        "http://intproj22.sit.kmutt.ac.th/",
        "http://localhost:5173/"
})
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
    public  User creatUser(@Valid  @RequestBody UserRequestDTO userObj) throws InterruptedException {
        return userService.creatUser(userObj);
    }

    @PutMapping("/{userid}")
    public  User updateUser(@PathVariable Integer userid ,@Valid @RequestBody UserRequestDTO userObj) throws InterruptedException {
        return userService.updateUser(userid,userObj);
    }

    @DeleteMapping("/{id}")
    public  void  delete(@PathVariable Integer id) {
        userService.deleteUser(id);
    }


}
