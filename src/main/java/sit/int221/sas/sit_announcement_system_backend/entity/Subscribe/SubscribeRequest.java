package sit.int221.sas.sit_announcement_system_backend.entity.Subscribe;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class SubscribeRequest  implements Serializable {
    @NotNull
    private String [] subscribes ;
}
