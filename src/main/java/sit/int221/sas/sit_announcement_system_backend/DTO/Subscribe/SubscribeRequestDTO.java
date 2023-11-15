package sit.int221.sas.sit_announcement_system_backend.DTO.Subscribe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SubscribeRequestDTO implements Serializable {
    @NotNull
    private Integer [] subscribes ;

    @NotBlank(message = "must not be blank")
    @Size(min = 1, max = 150)
    @Email(message = "Email should be valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email ;
    public String getEmail(){
        return email.trim();
    }

}
