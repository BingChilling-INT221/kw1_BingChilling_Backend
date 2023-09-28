package sit.int221.sas.sit_announcement_system_backend.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.sas.sit_announcement_system_backend.entity.User;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundByfield;
import sit.int221.sas.sit_announcement_system_backend.repository.UserRepo.UserRepository;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository ;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userexist = userRepository.findByUsername(username).orElse(null) ;
        if (userexist!=null) {
            return new org.springframework.security.core.userdetails.User(userexist.getUsername(),userexist.getPassword(),
                    new ArrayList<>());
        } else {
            throw new NotfoundByfield(username+" not found ","username");
        }
    }
}
