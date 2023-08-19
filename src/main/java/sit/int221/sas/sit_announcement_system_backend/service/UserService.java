package sit.int221.sas.sit_announcement_system_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundById;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepository;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.time.ZoneOffset.UTC;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository ;
    public List<User> getAllUser(){

        return userRepository.findAll(Sort.by("role").ascending().and(Sort.by("username")));
    }

    public User getDetailUser(Integer userid){
       return userRepository.findById(userid).orElseThrow(()->new NotfoundById((userid+" doesn't not exist"),"id"));
    }

    public User creatUser (UserRequestDTO user) throws InterruptedException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User userObj = new User() ;
        userObj.setUsername(user.getUsername().trim());
        userObj.setName(user.getName().trim());
        userObj.setEmail(user.getEmail().trim());
        userObj.setRole(Role.valueOf(user.getRole().trim()));
        return userRepository.saveAndFlush(userObj);
    }

    public User updateUser(Integer userid,UserRequestDTO user){
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        User userExist = userRepository.findById(userid).orElseThrow(()->new NotfoundById((userid+" doesn't not exist"),"id"));
        userExist.setUsername(user.getUsername());
        userExist.setName(user.getName());
        userExist.setEmail(user.getEmail());
        userExist.setRole(Role.valueOf(user.getRole()));
    return userRepository.saveAndFlush(userExist);
    }
}
