package sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import sit.int221.sas.sit_announcement_system_backend.utils.AnnouncementDisplay;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

@Getter
@Setter
public class UserRequestDTO {
    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 45)
    private String username ;

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100)
    private String name ;

    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    private String email ;

    //เหลือ check Enum
    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    private String role ;
//    public Role getRole() {
//        if(role!=null || role) {
//            return   AnnouncementDisplay.valueOf(announcementDisplay);
//        }
//        else {
//            return AnnouncementDisplay.N;
//        }
//    }
}
