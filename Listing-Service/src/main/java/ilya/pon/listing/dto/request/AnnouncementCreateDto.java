package ilya.pon.listing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AnnouncementCreateDto {
    @NotNull
    @NotBlank
    private String title;
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private String categoryId;
    @NotNull
    private UUID userId;
}