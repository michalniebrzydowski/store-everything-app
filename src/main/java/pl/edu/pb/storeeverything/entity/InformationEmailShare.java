package pl.edu.pb.storeeverything.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "information_sharing")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InformationEmailShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sharing_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private Information information;

    @ManyToOne
    @JoinColumn(name = "shared_with_user_id")
    private User user;

    @Column(name = "edit_permission")
    private Boolean editPermission;
}
