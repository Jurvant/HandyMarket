package ilya.pon.listing.dto.request;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AnnouncementFilterDto {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Status status;
    private Category category;
    private LocalDateTime createdAt;
}
