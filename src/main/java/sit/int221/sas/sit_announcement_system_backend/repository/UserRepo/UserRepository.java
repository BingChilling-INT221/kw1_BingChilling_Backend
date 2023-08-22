package sit.int221.sas.sit_announcement_system_backend.repository.UserRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer>,CustomUserRepository {

    User findByUsername(String username) ;

}
