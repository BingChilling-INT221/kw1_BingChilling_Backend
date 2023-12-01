package sit.int221.sas.sit_announcement_system_backend.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

public enum Role {
    admin, announcer, visitor;

    @Component("Role")
    @Getter
    static class RoleComponent {
        private final String ADMIN = Role.admin.name();
        private final String ANNOUNCER = Role.announcer.name();
        private final String VISITOR = Role.visitor.name();
    }
}
