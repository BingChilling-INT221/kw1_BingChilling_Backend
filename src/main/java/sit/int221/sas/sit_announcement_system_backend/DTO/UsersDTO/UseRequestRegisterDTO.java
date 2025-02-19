package sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckRole;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckUnique;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;


@Getter
@Setter
public class UseRequestRegisterDTO {
    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 45)
    @CheckUnique(columnName = "username")
    private String username;

    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 100)
    @CheckUnique(columnName = "name")
    private String name;

    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    @Email(message = "Email should be valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @CheckUnique(columnName = "email")
    private String email;

    //เหลือ check Enum
//    @NotNull(message = "must not be null")
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

    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,14}$",
            message = "must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters")
    private String password;
}
