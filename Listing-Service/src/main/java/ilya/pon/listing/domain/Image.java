package ilya.pon.listing.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    private UUID id;

    @Column(name = "image_description")
    private String description;

    @Column(name = "image_url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
}