package sit.int221.sas.sit_announcement_system_backend.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ApplicationConfig {
    @Bean
    public static ListMapper listMapper() {
        return ListMapper.getInstance();

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @ExceptionHandler(value = { ResourceNotFoundException.class })
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ResponseBody
//    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(404,ex.getMessage(), ex.getMessage().getClass().getSimpleName());
//        errorResponse.setTimestamp(new DateTimeException(""));
//        return errorResponse;
//    }
}

