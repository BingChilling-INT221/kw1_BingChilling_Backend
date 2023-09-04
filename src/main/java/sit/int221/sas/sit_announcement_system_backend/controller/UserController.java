package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.*;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
//แก้ Cross origin ให้กำหนด port
@CrossOrigin(origins = {
        "http://intproj22.sit.kmutt.ac.th/",
        "https://intproj22.sit.kmutt.ac.th/",
        "http://localhost:5173/",
        "https://localhost:5173/",
        "http://ip22kw1.sit.kmutt.ac.th/",
        "https://ip22kw1.sit.kmutt.ac.th/",
})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper ;
    @GetMapping("")
    public List<UserResponseDTO> getUsers(){
        return   listMapper.mapList(userService.getAllUser(),UserResponseDTO.class,modelMapper) ;
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserDetail(@PathVariable Integer id){
        return modelMapper.map(userService.getDetailUser(id),UserResponseDTO.class);
    }

    @PostMapping("")
    public UserResponseDTO creatUser(@Valid  @RequestBody UseRequestRegisterDTO userObj) throws InterruptedException {
        return modelMapper.map(userService.createUser(userObj),UserResponseDTO.class);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Integer id ,@Valid @RequestBody UserUpdateRequestDTO userObj) throws InterruptedException {
        return  modelMapper.map(userService.updateUser(id,userObj),UserResponseDTO.class);
    }

    @DeleteMapping("/{id}")
    public  void  delete(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @PostMapping("/match")
    public void   matchPassword(@Valid @RequestBody UserLoginRequestDTO userLogin){
        userService.matchPassword(userLogin.getUsername(),userLogin.getPassword()) ;
    }

}
