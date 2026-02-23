package ilya.pon.listing.dto.wrapper;

import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.ImageCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateAnnounceImageDto(
        @NotNull @Valid
        AnnouncementCreateDto announcementDto,

        @NotNull @NotEmpty @Valid
        List<ImageCreateDto> imageDto
) { }
