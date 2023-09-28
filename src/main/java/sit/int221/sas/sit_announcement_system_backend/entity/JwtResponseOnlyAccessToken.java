package sit.int221.sas.sit_announcement_system_backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtResponseOnlyAccessToken  implements Serializable {
        private static final long serialVersionUID = -8091879091924046844L;
        private final String token ;

}
