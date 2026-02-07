package ilya.pon.listing.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
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

    @OneToMany(mappedBy = "category")
    List<Announcement> announcements;
}