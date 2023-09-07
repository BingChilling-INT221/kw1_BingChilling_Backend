package sit.int221.sas.sit_announcement_system_backend.execeptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private DateTimeException timestamp;
    private final int status;
    private final String message;
    private final String instance;
    private String stackTrace;
    private List<ValidationError> detail;

    public void addValidationError(String field, String message) {
        if (Objects.isNull(detail)) {
            detail = new ArrayList<>();
        }
        detail.add(new ValidationError(field, message));
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String errorMessage;
    }


}
