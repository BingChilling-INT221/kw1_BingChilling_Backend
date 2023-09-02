package sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id ;
    private String username ;
    private String name ;
    private String email ;
    private Role role ;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;

}
