package sit.int221.sas.sit_announcement_system_backend.execeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.SetFiledErrorException;
import sit.int221.sas.sit_announcement_system_backend.execeptions.customError.NotfoundById;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //มีอันที่รับทุก Error
    //หากส่ง Error มาเหมือนกัน คือเป็น Class แต่ไม่ใช่ Error ที่เราเซตไว้
    //มีอีก Error ที่รับทั้งหมด ทั้ง class และ filed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidateError(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            //String fieldName = error.getObjectName();
            String fieldName = error.getArguments()[error.getArguments().length-1].toString();
            if (error instanceof FieldError)  fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            er.addValidationError(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
//ปัญหามีอยู่ว่า หากมีเออเร่อก่อน ที่จะเช็คตาม Custom จะทำไง ?
    // ตั้งชื่อ Error ไม่สื่อ

    @ExceptionHandler(SetFiledErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNullPointer (SetFiledErrorException e, WebRequest request){
        ErrorResponse er = new ErrorResponse(HttpStatus.NOT_FOUND.value(),e.getMessage(),request.getDescription(false)) ;
        er.addValidationError(e.getAdditionalField1(),e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(NotfoundById.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  ResponseEntity<ErrorResponse> handleNotFound(NotfoundById e , WebRequest request){
    ErrorResponse er = new ErrorResponse(HttpStatus.NOT_FOUND.value(),e.getMessage(),request.getDescription(false)) ;
    er.addValidationError(e.getField(),e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er) ;
    }


    //    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<ErrorResponse>  handlerAuthentication(RuntimeException e){
//        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//        ErrorResponse er = new ErrorResponse(LocalDateTime.now().format(formatter),HttpStatus.NOT_FOUND.value(),e.getMessage(), e.getMessage().getClass().getSimpleName());
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er) ;
//    }

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRequestParam(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
        fieldErrors.stream().forEach(x-> er.addValidationError(x.getField(),x.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er) ;
    }*/

}
