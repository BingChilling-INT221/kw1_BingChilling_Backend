package sit.int221.sas.sit_announcement_system_backend.DTO.Subscribe;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckUnique;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SubscribeRequestToUnsubscribeDTO {
    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    @Email(message = "Email should be valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email ;
    private Integer [] subscribes ;
}
