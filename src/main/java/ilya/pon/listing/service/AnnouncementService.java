package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.AnnouncementCreateDto;
import ilya.pon.listing.mapper.AnnouncementCreateMapper;
import ilya.pon.listing.repository.AnnouncementRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
    private final AnnouncementRepository repo;
    private final AnnouncementCreateMapper createMapper;
    private final CategoryService categoryService;

    public AnnouncementService(AnnouncementRepository repo, AnnouncementCreateMapper createMapper,
                               CategoryService categoryService) {
        this.repo = repo;
        this.createMapper = createMapper;
        this.categoryService = categoryService;
    }

    public Announcement save(@NotNull @NotBlank AnnouncementCreateDto dto) {
        return createMapper.toEntity(dto, categoryService);
    }
}