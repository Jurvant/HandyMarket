package ilya.pon.listing.dto.response;

import java.util.UUID;

public record ImageResponseDto (
        UUID id,
        String url,
        String description
){
}
