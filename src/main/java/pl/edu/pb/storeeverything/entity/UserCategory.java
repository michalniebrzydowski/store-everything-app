package pl.edu.pb.storeeverything.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_categories_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
