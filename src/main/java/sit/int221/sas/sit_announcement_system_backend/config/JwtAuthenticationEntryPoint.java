package sit.int221.sas.sit_announcement_system_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sit.int221.sas.sit_announcement_system_backend.execeptions.ErrorResponse;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.JwtErrorException;

import java.io.IOException;
import java.io.Serializable;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        ErrorResponse er = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), request.getRequestURI());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(), er);
    }

//    public void commence(HttpServletRequest request, HttpServletResponse response, JwtErrorException e) throws IOException {
//        ErrorResponse er = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), request.getRequestURI());
//        er.addValidationError(e.getField(), e.getMessage());
//        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
//
//    }
}
