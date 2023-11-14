package sit.int221.sas.sit_announcement_system_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import sit.int221.sas.sit_announcement_system_backend.entity.SubscribeFolder.CompositekeySubscrib;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(CompositekeySubscrib.class)
@Table(name = "subscribe")
public class Subscribe implements Serializable {
    @Id
    @Column(name = "email")
    private String email ;
    @Id
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category ;




}
