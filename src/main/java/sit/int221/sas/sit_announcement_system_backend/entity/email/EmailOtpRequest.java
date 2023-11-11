package sit.int221.sas.sit_announcement_system_backend.entity.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class EmailOtpRequest implements Serializable {
    private final String tokenOtp;
    private final String otpNumber;
}
