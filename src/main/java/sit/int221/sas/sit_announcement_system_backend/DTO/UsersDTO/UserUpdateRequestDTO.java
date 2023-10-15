package sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckRole;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckUnique;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

@Getter
@Setter
public class UserUpdateRequestDTO {
    //    @JsonIgnore
//    @Autowired
//    private BindingResult bindingResult; // Inject BindingResult
//    public BindingResult getBindingResult() {
//        return bindingResult;
//    }
//    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 45)
    @CheckUnique(columnName = "username")
    private String username;

    public String getUsername() {
        return username.trim();
    }


    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100)
    @CheckUnique(columnName = "name")
    private String name;

    public String getName() {
        return name.trim();
    }

    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    @CheckUnique(columnName = "email")
    @Email(message = "Email should be valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    public String getEmail() {
        return email.trim();
    }

    //เหลือ check Enum
    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")

    @CheckRole
    private String role;

    public Role getRole() {
        if (role != null) {
            return Role.valueOf(role.trim());
        } else {
            return Role.announcer;
        }
    }
}
