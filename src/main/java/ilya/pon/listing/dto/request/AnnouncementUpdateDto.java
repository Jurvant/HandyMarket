package ilya.pon.listing.dto.request;

import ilya.pon.listing.domain.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnnouncementUpdateDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Category category;
}