package sit.int221.sas.sit_announcement_system_backend.repository.UserRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import sit.int221.sas.sit_announcement_system_backend.entity.User;

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional //method needs to be executed within a transaction
    public User RefreshUser(User user) {
        try {
            entityManager.persist(user);
            entityManager.refresh(user);
        } catch (Exception e) {
            // Handle the exception
        } finally {
            entityManager.detach(user); //หยุดทำงาน
            entityManager.flush();
            //   entityManager.close(); ไม่แนะนำ ปล่อยหมด
        }
        return user;
    }

}
