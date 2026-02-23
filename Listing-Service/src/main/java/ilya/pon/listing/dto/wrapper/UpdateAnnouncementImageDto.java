package ilya.pon.listing.dto.wrapper;

import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.dto.request.ImageCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateAnnouncementImageDto (
        @NotNull @Valid
        AnnouncementUpdateDto announcementDto,
        @Valid
        List<ImageCreateDto> imageDto
){}