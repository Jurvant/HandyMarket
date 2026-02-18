package ilya.pon.listing.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AnnouncementResponseDto(
        UUID id,
        UUID userId,
        String title,
        String description,
        BigDecimal price,
        LocalDateTime createdAt,
        String status,
        String categoryName
){ }