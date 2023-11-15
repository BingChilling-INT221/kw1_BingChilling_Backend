package sit.int221.sas.sit_announcement_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder.CompositekeySubscrib;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(CompositekeySubscrib.class)
@Table(name = "subscribe")
public class Subscribe implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="categoryId", nullable=false)
    private Category category ;

    @Id
    @Column(name = "subscriberEmail", nullable=false)
    private String email ;

    @Column(name = "createdOn",insertable = false, updatable = false)
    private ZonedDateTime createdOn ;
    @Column(name = "updatedOn",insertable = false, updatable = false)
    private ZonedDateTime updatedOn ;



}
