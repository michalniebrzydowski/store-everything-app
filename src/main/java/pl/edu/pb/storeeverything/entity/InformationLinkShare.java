package pl.edu.pb.storeeverything.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "information_link_sharing")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InformationLinkShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_sharing_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private Information information;

    @Column(name = "edit_permission")
    private Boolean editPermission;

    @Column(name = "share_link")
    private String shareLink;
}
