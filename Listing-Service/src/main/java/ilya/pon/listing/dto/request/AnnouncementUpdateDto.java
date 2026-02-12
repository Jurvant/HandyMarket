package ilya.pon.listing.dto.request;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementUpdateDto {
    private String title;
    private String description;
    private BigDecimal price;
    private Category category;
    private Status status;
}