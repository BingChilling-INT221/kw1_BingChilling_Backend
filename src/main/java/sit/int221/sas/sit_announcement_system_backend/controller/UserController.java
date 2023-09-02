package sit.int221.sas.sit_announcement_system_backend.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserLoginRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserResponseDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.AuthenticationErrorException;
import sit.int221.sas.sit_announcement_system_backend.service.UserService;
import sit.int221.sas.sit_announcement_system_backend.utils.ListMapper;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{userid}")
    public UserResponseDTO getUserDetail(@PathVariable Integer userid){
        return modelMapper.map(userService.getDetailUser(userid),UserResponseDTO.class);
    }

    @PostMapping("")
    public UserResponseDTO creatUser(@Valid  @RequestBody UserRequestDTO userObj) throws InterruptedException {
        return modelMapper.map(userService.createUser(userObj),UserResponseDTO.class);
    }

    @PutMapping("/{userid}")
    public UserResponseDTO updateUser(@PathVariable Integer userid ,@Valid @RequestBody UserRequestDTO userObj) throws InterruptedException {
        return  modelMapper.map(userService.updateUser(userid,userObj),UserResponseDTO.class);
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
