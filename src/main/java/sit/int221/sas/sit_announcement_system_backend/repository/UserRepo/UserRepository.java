package sit.int221.sas.sit_announcement_system_backend.repository.UserRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, CustomUserRepository {

    Optional<User> findByUsername(String username);


    @Query("SELECT u FROM User u " +
            "WHERE  (u.username = :filed " +
            "OR  u.name = :filed " +
            "OR  u.email = :filed )" +
            "AND (:id is null  OR u.id <> :id) ")
    Optional<User> findUsersByUsernameAndNameAndEmail(
            @Param("filed") String filed, @Param("id") Integer id
    );

   Optional<User> findByEmail(String email);
}
