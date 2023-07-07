package pl.edu.pb.storeeverything.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "information")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
@Builder
public class Information {

    @Column(name = "information_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "link")
    private String link;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "information")
    private Set<InformationEmailShare> informationSharing;

    @OneToMany(mappedBy = "information")
    private Set<InformationLinkShare> linkShares;
}