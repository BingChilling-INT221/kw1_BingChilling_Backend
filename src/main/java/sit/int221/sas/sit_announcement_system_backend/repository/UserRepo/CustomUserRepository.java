package sit.int221.sas.sit_announcement_system_backend.repository.UserRepo;

import org.springframework.transaction.annotation.Transactional;
import sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO.UserRequestDTO;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

public interface CustomUserRepository {
    User RefreshUser(User user) ;

}
