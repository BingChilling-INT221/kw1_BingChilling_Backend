package sit.int221.sas.sit_announcement_system_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    private Integer id ;
    @Column(name="username",unique = true, nullable = false,length = 45)
    private String username ;
    @Column(name="name",unique = true , nullable = false,length = 100)
    private String name ;
    @Column(name="email",unique = true , nullable = false,length = 150)
    private String email ;
    @Enumerated(EnumType.STRING)
    private Role role ;

    @Column(name = "createdOn",nullable = true, insertable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdOn;
    @Column(name = "updateOn",nullable = true ,insertable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime updatedOn;

}
