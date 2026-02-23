package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Image;
import ilya.pon.listing.dto.request.ImageCreateDto;
import ilya.pon.listing.mapper.request.ImageRequestMapper;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.ImageRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository repo;
    private final ImageRequestMapper mapper;
    private final AnnouncementRepository announcementRepository;
    public ImageService(ImageRepository repo, ImageRequestMapper mapper, AnnouncementRepository announcementRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.announcementRepository = announcementRepository;
    }

    public Image save(ImageCreateDto dto, @NotNull Announcement announcementId) {
        Image image = mapper.toEntity(dto);
        image.setAnnouncement(announcementId);
        return repo.save(image);
    }

    public Announcement deleteImageFromAnnouncement(Announcement announcement, UUID imageId) {
        Image image = announcement.getImages().stream().filter(i -> i.getId().equals(imageId)).findFirst().orElse(null);
        announcement.getImages().remove(image);
        announcementRepository.save(announcement);
        return announcement;
    }
}