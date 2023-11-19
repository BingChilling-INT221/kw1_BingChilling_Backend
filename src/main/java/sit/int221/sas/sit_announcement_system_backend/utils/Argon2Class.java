package sit.int221.sas.sit_announcement_system_backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Argon2Class {
    private final Argon2PasswordEncoder arg2SpringSecurity =  new Argon2PasswordEncoder(16, 30, 1, 16, 2);

    public Argon2PasswordEncoder  getArg2SpringSecurity (){
        return this.arg2SpringSecurity;
    }
    public String getEncryption(String input){
       return this.arg2SpringSecurity.encode(input);
    }

    public Boolean checkMatchInput(String input,String key){
       return  this.arg2SpringSecurity.matches(input, key);
    }
}
