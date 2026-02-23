package ilya.pon.listing.dto.wrapper;

import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import ilya.pon.listing.dto.response.ImageResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ResponseAnnouncementImageDto (
        @NotNull @Valid
        AnnouncementResponseDto announcementDto,

        @NotNull @NotEmpty @Valid
        List<ImageResponseDto> imageDto
){}
