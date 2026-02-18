package ilya.pon.listing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record AnnouncementCreateDto(
        @NotNull
        @NotBlank
        String title,
        String description,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        String categoryId,
        @NotNull
        UUID userId
) { }