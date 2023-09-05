package sit.int221.sas.sit_announcement_system_backend.DTO.UsersDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDTO {
    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    private String username;
    //    @NotNull(message = "must not be null")
    @NotBlank(message = "must not be blank")
    private String password;
}
