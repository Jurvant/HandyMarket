package ilya.pon.listing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AnnouncementCreateDto {
    @NotNull
    @NotBlank
    private String title;
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private UUID categoryId;
    @NotNull
    private UUID userId;
}