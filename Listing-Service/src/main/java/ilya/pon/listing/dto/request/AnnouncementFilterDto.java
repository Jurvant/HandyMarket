package ilya.pon.listing.dto.request;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementFilterDto {
    private UUID userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Status status;
    private Category category;
    private LocalDateTime createdAt;
}
