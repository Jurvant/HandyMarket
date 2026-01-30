package ilya.pon.listing.service;

import ilya.pon.listing.custom.exception.NoAccesToChangeDataException;
import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.additions.Status;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.mapper.request.AnnouncementRequestMapper;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.custom.AnnouncementCustomRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnnouncementService {
    private final AnnouncementRepository repo;
    private final AnnouncementRequestMapper requestMapper;
    private final CategoryService categoryService;
    private final AnnouncementCustomRepository customRepository;

    public AnnouncementService(AnnouncementRepository repo, AnnouncementRequestMapper requestMapper,
                               CategoryService categoryService, AnnouncementCustomRepository customRepository) {
        this.repo = repo;
        this.requestMapper = requestMapper;
        this.categoryService = categoryService;
        this.customRepository = customRepository;
    }

    public Announcement save(@NotNull @NotBlank AnnouncementCreateDto dto) {
        return repo.save(requestMapper.toEntity(dto, categoryService));
    }

    public Announcement findById(@NotNull @NotBlank UUID id) {
        return repo.findById(id).orElseThrow(EntityExistsException::new);
    }

    public void deactivateAnouncement(@NotNull UUID announcementId,@NotNull @NotBlank UUID userId) {
        Announcement announcement = findById(announcementId);
        if(userId.equals(announcement.getUserId())) {
            announcement.setStatus(Status.DEACTIVATED);
            repo.save(announcement);
        }else{
            throw new NoAccesToChangeDataException("User is not allowed to deactivate this announcement");
        }
    }
    public void activateAnouncement(@NotNull UUID announcementId,@NotNull @NotBlank UUID userId) {
        Announcement announcement = findById(announcementId);
        if(userId.equals(announcement.getUserId())) {
            announcement.setStatus(Status.ACTIVE);
            repo.save(announcement);
        }else{
            throw new NoAccesToChangeDataException("User is not allowed to activate this announcement");
        }
    }

    public Page<Announcement> findByParameters(AnnouncementFilterDto dto, Pageable pageable){
        return customRepository.findByParameters(dto.getId(), dto.getUserId(),
                dto.getTitle(),dto.getDescription(), dto.getPrice(), dto.getStatus(),
                dto.getCategory(), dto.getCreatedAt(), pageable);
    }

    public void deleteById(@NotNull UUID id,@NotNull UUID userId) {
        Announcement announcement = findById(id);
        if(announcement.getUserId().equals(userId)) {
            repo.deleteById(id);
        }else throw new NoAccesToChangeDataException("User is not allowed to delete this announcement");
    }

    public Page<Announcement> search(String text, Pageable pageable) {
        return customRepository.search(text, pageable);
    }

    public Announcement update(AnnouncementUpdateDto dto,  UUID id, UUID userId) {
        Announcement announcement = findById(id);
        if(!userId.equals(announcement.getUserId())) {
            throw new NoAccesToChangeDataException("User is not allowed to update this announcement");
        }
        requestMapper.update(dto, announcement);
        repo.save(announcement);
        return announcement;
    }

    public Page<Announcement> findByUserId(UUID userId, Pageable pageable) {
        return repo.findByUserId(userId, pageable);
    }
}