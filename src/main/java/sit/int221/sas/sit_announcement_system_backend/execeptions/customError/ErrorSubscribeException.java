package sit.int221.sas.sit_announcement_system_backend.execeptions.customError;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@Setter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorSubscribeException  extends Exception{
    private String field;
    private List<Integer> list ;
    public ErrorSubscribeException(String message,List<Integer> list) {
        super(message);
        this.list=list;

    }
}
