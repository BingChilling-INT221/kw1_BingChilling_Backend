package sit.int221.sas.sit_announcement_system_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.AuthenticationErrorException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.util.List;

@Service
public class UserService {
   private final Argon2PasswordEncoder arg2SpringSecurity = new Argon2PasswordEncoder(16, 100, 1, 60000, 10);
    @Autowired
    private UserRepository userRepository ;
    public List<User> getAllUser(){

        return userRepository.findAll(Sort.by("role").ascending().and(Sort.by("username")));
    }

    public User getDetailUser(Integer userid){
       return userRepository.findById(userid).orElseThrow(()->new NotfoundByfield((userid+" does not exist"),"id"));
    }

    @Transactional
    public User createUser (UserRequestDTO user) throws InterruptedException {
        User userObj = new User() ;
        userObj.setUsername(user.getUsername().trim());
        userObj.setName(user.getName().trim());
        userObj.setEmail(user.getEmail().trim());
        userObj.setRole(user.getRole());

        String savedPassword=this.arg2SpringSecurity.encode(user.getPassword().trim());
        userObj.setPassword(savedPassword);
        return    userRepository.RefreshUser(userObj);
    }
    @Transactional
    public User updateUser(Integer userid,UserRequestDTO user) throws InterruptedException{
        User userExist = userRepository.findById(userid).orElseThrow(()->new NotfoundByfield(userid+" does not exist","id") );
        userExist.setUsername(user.getUsername().trim());
        userExist.setName(user.getName().trim());
        userExist.setEmail(user.getEmail().trim());
        userExist.setRole(user.getRole());
        userRepository.saveAndFlush(userExist);
        userRepository.RefreshUser(userExist);
     return   userExist ;

    }

    public void deleteUser(Integer userid){
        userRepository.findById(userid).orElseThrow(()-> new NotfoundByfield((userid+"does not exist"),"id")) ;
        userRepository.deleteById(userid);
    }

    public void matchPassword(String username, String raw_password) {

            User userExist = userRepository.findByUsername(username.trim()).orElseThrow(() -> new NotfoundByfield(username + " DOES NOT exists", "username"));
            if (!this.arg2SpringSecurity.matches(raw_password.trim(), userExist.getPassword())) {
                throw new AuthenticationErrorException("Password Not Matched");
            }
        }
    }
