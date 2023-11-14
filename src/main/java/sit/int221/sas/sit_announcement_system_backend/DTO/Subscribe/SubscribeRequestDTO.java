package sit.int221.sas.sit_announcement_system_backend.DTO.Subscribe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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

}
