package sit.int221.sas.sit_announcement_system_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.sas.sit_announcement_system_backend.entity.Announcement;
import sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder.CompositekeySubscrib;
import sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;

import java.time.ZonedDateTime;
import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, CompositekeySubscrib> {

    List<Subscribe> findByEmail(String email);

    List<Subscribe> findByCategory_CategoryId(Integer id);
    @Query(value = "SELECT a FROM Subscribe a WHERE    a.email= :email AND a.category.categoryId IN :categoryIds")
    List<Subscribe> findByEmailAndCategory_CategoryId(@Param("email") String email,@Param("categoryIds") Integer [] categoryIds);



}
