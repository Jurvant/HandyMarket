package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.wrapper.CreateAnnounceImageDto;
import ilya.pon.listing.dto.wrapper.ResponseAnnouncementImageDto;
import ilya.pon.listing.dto.wrapper.UpdateAnnouncementImageDto;
import ilya.pon.listing.exception.NoAccesToChangeDataException;
import ilya.pon.listing.mapper.MapperMaster;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.custom.AnnouncementCustomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository repo;
    private final CategoryService categoryService;
    private final AnnouncementCustomRepository customRepository;
    private final MapperMaster mapper;

    @Transactional
    public ResponseAnnouncementImageDto save(@NotNull @NotBlank @Valid CreateAnnounceImageDto dto, @NotNull Jwt jwt) {
        UUID userId = extractUserId(jwt);
        Announcement announcement = mapper.toEntity(dto.announcementDto(), userId, categoryService);
        if (dto.imageDto() != null) {
            dto.imageDto().forEach(imageDto -> {
                announcement.addImage(mapper.toEntity(imageDto));
            });
        }
        Announcement savedAnnouncement = repo.save(announcement);
        return mapToResponseDto(savedAnnouncement);
    }

    @Transactional(readOnly = true)
    public ResponseAnnouncementImageDto findDtoById(@NotNull @NotBlank UUID id) {
        Announcement announcement = repo.findByIdWithImages(id)
                .orElseThrow(EntityNotFoundException::new);
        return mapToResponseDto(announcement);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAnnouncementImageDto> findByParameters(AnnouncementFilterDto dto, Pageable pageable) {
        Page<Announcement> announcements = customRepository.findByParameters(dto.getUserId(),
                dto.getTitle(), dto.getDescription(), dto.getPrice(), dto.getStatus(),
                dto.getCategory(), dto.getCreatedAt(), pageable);
        return announcements.map(this::mapToResponseDto);
    }

    @Transactional
    public void deleteById(@NotNull UUID id, @NotNull Jwt jwt) {
        UUID userId = extractUserId(jwt);
        Announcement announcement = findById(id);

        if (!announcement.getUserId().equals(userId)) {
            throw new NoAccesToChangeDataException("User is not allowed to delete this announcement");
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAnnouncementImageDto> search(String text, Pageable pageable) {
        return customRepository.search(text, pageable).map(this::mapToResponseDto);
    }

    @Transactional
    public ResponseAnnouncementImageDto update(@Valid UpdateAnnouncementImageDto dto, UUID id, Jwt jwt) {
        UUID userId = extractUserId(jwt);
        Announcement announcement = repo.findByIdWithImages(id)
                .orElseThrow(EntityNotFoundException::new);

        if (!userId.equals(announcement.getUserId())) {
            throw new NoAccesToChangeDataException("User is not allowed to update this announcement");
        }
        mapper.update(dto.announcementDto(), announcement);
        if (dto.imageDto() != null) {
            announcement.getImages().clear();
            dto.imageDto().forEach(imageDto -> {
                announcement.addImage(mapper.toEntity(imageDto));
            });
        }
        repo.save(announcement);
        return mapToResponseDto(announcement);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAnnouncementImageDto> findByUserId(UUID userId, Pageable pageable) {
        return repo.findByUserId(userId, pageable).map(this::mapToResponseDto);
    }

    private UUID extractUserId(Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            throw new JwtValidationException("Invalid token", List.of(new OAuth2Error("invalid_token")));
        }
        return UUID.fromString(jwt.getSubject());
    }

    private Announcement findById(@NotNull @NotBlank UUID id) {
        return repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private ResponseAnnouncementImageDto mapToResponseDto(Announcement announcement) {
        return new ResponseAnnouncementImageDto(
                mapper.toResponseDto(announcement),
                announcement.getImages().stream().map(mapper::toResponseDto).toList()
        );
    }
}