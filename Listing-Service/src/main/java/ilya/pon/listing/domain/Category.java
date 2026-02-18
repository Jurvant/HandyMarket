package ilya.pon.listing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @Id
    @Column(name = "category_id")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "parent_id")
    private String parentId;

    @ToString.Exclude
    @OneToMany(mappedBy = "category")
    List<Announcement> announcements;
}