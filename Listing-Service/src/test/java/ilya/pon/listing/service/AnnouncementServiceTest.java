package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import ilya.pon.listing.dto.wrapper.UpdateAnnouncementImageDto;
import ilya.pon.listing.exception.NoAccesToChangeDataException;
import ilya.pon.listing.mapper.MapperMaster;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.custom.AnnouncementCustomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceTest {
    @InjectMocks
    AnnouncementService service;

    @Mock
    AnnouncementRepository repo;

    @Mock
    MapperMaster mapper;

    @Mock
    CategoryService categoryService;

    @Mock
    AnnouncementCustomRepository announcementCustomRepository;

    @Test
    void shouldNotPassVerification_in_deleteById(){
        UUID announceId = UUID.randomUUID();
        UUID originalId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", fakeId.toString())
                .build();

        Announcement announcement = new Announcement();
        announcement.setUserId(originalId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        assertThrows(NoAccesToChangeDataException.class, () -> service.deleteById(announceId, jwt));
    }

    @Test
    void shouldPassVerification_in_deleteById(){
        UUID announceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", userId.toString())
                .build();

        Announcement announcement = new Announcement();
        announcement.setUserId(userId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        service.deleteById(announceId, jwt);
        verify(repo, times(1)).deleteById(announceId);
    }

    @Test
    void shouldNotPassVerification_in_update(){
        UUID announceId = UUID.randomUUID();
        UUID originalId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", fakeId.toString())
                .build();

        Announcement announcement = new Announcement();
        announcement.setUserId(originalId);
        announcement.setId(announceId);
        when(repo.findByIdWithImages(announceId)).thenReturn(Optional.of(announcement));

        assertThrows(NoAccesToChangeDataException.class, () ->
                service.update(new UpdateAnnouncementImageDto(null, null), announceId, jwt));
    }

    @Test
    void shouldPassVerification_in_update(){
        UUID announceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UpdateAnnouncementImageDto dto = new UpdateAnnouncementImageDto(null, null);
        AnnouncementResponseDto responseDto = null;

        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", userId.toString())
                .build();

        Announcement announcement = new Announcement();
        announcement.setImages(new ArrayList<>());
        announcement.setUserId(userId);
        announcement.setId(announceId);
        when(repo.findByIdWithImages(announceId)).thenReturn(Optional.of(announcement));
        when(mapper.toResponseDto(announcement)).thenReturn(responseDto);

        service.update(dto, announceId, jwt);
        verify(repo, times(1)).save(announcement);
    }
}