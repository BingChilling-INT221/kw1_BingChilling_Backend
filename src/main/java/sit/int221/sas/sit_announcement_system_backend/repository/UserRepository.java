package sit.int221.sas.sit_announcement_system_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username) ;

}
