package sit.int221.sas.sit_announcement_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import sit.int221.sas.sit_announcement_system_backend.execeptions.usersValidation.CheckUnique;
import sit.int221.sas.sit_announcement_system_backend.utils.Role;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user"
//        ,uniqueConstraints = {
//        @UniqueConstraint( columnNames={"username"} ),
//        @UniqueConstraint( columnNames={"name"} ),
//        @UniqueConstraint( columnNames={"email"} )
//}
)
//ใส่ตรง @ ตรงนี้ไม่ได้ทำให้ หา repo ไม่เจอ
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true)
    private Integer id;
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    //เอาเวลาจากเครื่อง Server
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "announcementOwner")
    private List<Announcement> announcements;

}
