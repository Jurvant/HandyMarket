package ilya.pon.listing.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    private UUID id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "parent_id")
    private UUID parentId;

    @OneToMany(mappedBy = "category")
    List<Announcement> announcements;

}