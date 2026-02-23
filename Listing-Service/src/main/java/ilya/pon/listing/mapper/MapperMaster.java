package ilya.pon.listing.mapper;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Image;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.dto.request.ImageCreateDto;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import ilya.pon.listing.dto.response.ImageResponseDto;
import ilya.pon.listing.mapper.request.AnnouncementRequestMapper;
import ilya.pon.listing.mapper.request.ImageRequestMapper;
import ilya.pon.listing.mapper.response.AnnouncementResponseMapper;
import ilya.pon.listing.mapper.response.ImageResponseMapper;
import ilya.pon.listing.service.CategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MapperMaster {
    private final AnnouncementResponseMapper announcementResponseMapper;
    private final AnnouncementRequestMapper announcementRequestMapper;

    private final ImageRequestMapper imageRequestMapper;
    private final ImageResponseMapper imageResponseMapper;

    public AnnouncementResponseDto toResponseDto(@NotNull Announcement announcement) {
        return announcementResponseMapper.toDto(announcement);
    }

    public ImageResponseDto toResponseDto(@NotNull Image image) {
        return imageResponseMapper.toDto(image);
    }

    public void update(AnnouncementUpdateDto dto, Announcement announcement) {
        announcementRequestMapper.update(dto, announcement);
    }

    public Announcement toEntity(@NotNull AnnouncementCreateDto dto, @NotNull UUID userId, @NotNull CategoryService service) {
        return announcementRequestMapper.toEntity(dto, userId, service);
    }

    public Image toEntity(@NotNull ImageCreateDto dto) {
        return imageRequestMapper.toEntity(dto);
    }
}
