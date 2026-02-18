package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import ilya.pon.listing.exception.NoAccesToChangeDataException;
import ilya.pon.listing.mapper.request.AnnouncementRequestMapper;
import ilya.pon.listing.mapper.response.AnnouncementResponseMapper;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.custom.AnnouncementCustomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnnouncementService {
    private final AnnouncementRepository repo;
    private final AnnouncementRequestMapper requestMapper;
    private final CategoryService categoryService;
    private final AnnouncementCustomRepository customRepository;
    private final AnnouncementResponseMapper responseMapper;

    public AnnouncementService(AnnouncementRepository repo, AnnouncementRequestMapper requestMapper,
                               CategoryService categoryService, AnnouncementCustomRepository customRepository, AnnouncementResponseMapper responseMapper) {
        this.repo = repo;
        this.requestMapper = requestMapper;
        this.categoryService = categoryService;
        this.customRepository = customRepository;
        this.responseMapper = responseMapper;
    }

    public AnnouncementResponseDto save(@NotNull @NotBlank AnnouncementCreateDto dto, @NotNull Jwt jwt) {
        if(jwt.getSubject() == null){
            throw new JwtValidationException("Invalid token", List.of(new OAuth2Error("invalid_token")));
        }
        Announcement announcement = repo.save(requestMapper.toEntity(dto, UUID.fromString(jwt.getSubject()), categoryService));
        return responseMapper.toDto(announcement);
    }

    public Announcement findById(@NotNull @NotBlank UUID id) {
        return repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public AnnouncementResponseDto findDtoById(@NotNull @NotBlank UUID id) {
        Announcement announcement = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        return responseMapper.toDto(announcement);
    }

    public Page<AnnouncementResponseDto> findByParameters(AnnouncementFilterDto dto, Pageable pageable){
            Page<Announcement> announcements = customRepository.findByParameters(dto.getUserId(),
                dto.getTitle(),dto.getDescription(), dto.getPrice(), dto.getStatus(),
                dto.getCategory(), dto.getCreatedAt(), pageable);
            return announcements.map(responseMapper::toDto);
    }

    public void deleteById(@NotNull UUID id,@NotNull UUID userId) {
        Announcement announcement = findById(id);
        if(announcement.getUserId().equals(userId)) {
            repo.deleteById(id);
        }else throw new NoAccesToChangeDataException("User is not allowed to delete this announcement");
    }
    public void deleteById(@NotNull UUID id, @NotNull Jwt jwt) {
        if(jwt.getSubject() == null){
            throw new JwtValidationException("Invalid token", List.of(new OAuth2Error("invalid_token")));
        }

        Announcement announcement = findById(id);
        if(announcement.getUserId().equals(UUID.fromString(jwt.getSubject()))) {
            repo.deleteById(id);
        }else throw new NoAccesToChangeDataException("User is not allowed to delete this announcement");
    }

    public Page<AnnouncementResponseDto> search(String text, Pageable pageable) {

        return customRepository.search(text, pageable).map(responseMapper::toDto);
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

    public AnnouncementResponseDto update(AnnouncementUpdateDto dto,  UUID id, Jwt jwt) {
        if(jwt.getSubject() == null){
            throw new JwtValidationException("Invalid token", List.of(new OAuth2Error("invalid_token")));
        }

        Announcement announcement = findById(id);
        UUID userID = UUID.fromString(jwt.getSubject());
        if(!userID.equals(announcement.getUserId())) {
            throw new NoAccesToChangeDataException("User is not allowed to update this announcement");
        }
        requestMapper.update(dto, announcement);
        repo.save(announcement);
        return responseMapper.toDto(announcement);
    }

    public Page<AnnouncementResponseDto> findByUserId(UUID userId, Pageable pageable) {
        return repo.findByUserId(userId, pageable).map(responseMapper::toDto);
    }
}