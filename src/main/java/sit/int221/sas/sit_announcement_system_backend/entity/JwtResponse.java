package sit.int221.sas.sit_announcement_system_backend.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
//เอาไว้เก็บข้อมูลที่ส่งกลับไปหา client
//This is class is required for creating a response containing the JWT
@Getter
@Setter
@RequiredArgsConstructor //ต้องการ construc ของตัวนี้
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String accesstoken ;
    private final String refreshtoken ;


}

