package sit.int221.sas.sit_announcement_system_backend.repository.UserRepo;

import jakarta.validation.groups.Default;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "WHERE (:filed is null OR u.username = :filed) " +
            "OR  u.name = :filed " +
            "OR  u.email = :filed ")
    Optional<User> findUsersByUsernameAndNameAndEmail(
            @Param("filed") String filed
    );
}
