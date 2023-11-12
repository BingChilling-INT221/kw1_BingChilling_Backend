package sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SubscribeRequest  implements Serializable {
    @NotNull
    private Integer [] subscribes ;
//    public Integer[] getSubscribes(){
//        System.out.println((Integer[]) Arrays.stream(subscribes).map((x)->Integer.parseInt(x)).toArray());
//        return (Integer[]) Arrays.stream(subscribes).map((x)->Integer.parseInt(x)).toArray();
//    }
}
