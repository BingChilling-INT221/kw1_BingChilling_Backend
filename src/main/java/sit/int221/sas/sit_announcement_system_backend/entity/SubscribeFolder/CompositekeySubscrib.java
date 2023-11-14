package sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;

import java.io.Serializable;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CompositekeySubscrib implements Serializable {
    private  String email ;
    private Category category ;
}
