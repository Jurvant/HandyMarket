package ilya.pon.listing.service;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.exception.NoAccesToChangeDataException;
import ilya.pon.listing.mapper.request.AnnouncementRequestMapper;
import ilya.pon.listing.repository.AnnouncementRepository;
import ilya.pon.listing.repository.custom.AnnouncementCustomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

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
    AnnouncementRequestMapper requestMapper;

    @Mock
    CategoryService categoryService;

    @Mock
    AnnouncementCustomRepository announcementCustomRepository;

    @Test
    void shouldNotPassVerification_in_deleteById(){
        UUID announceId = UUID.randomUUID();
        UUID originalId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setUserId(originalId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        assertThrows(NoAccesToChangeDataException.class, () -> service.deleteById(announceId, fakeId));
    }

    @Test
    void shouldPassVerification_in_deleteById(){
        UUID announceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Announcement announcement = new Announcement();
        announcement.setUserId(userId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        service.deleteById(announceId, userId);
        verify(repo, times(1)).deleteById(announceId);
    }

    @Test
    void shouldNotPassVerification_in_update(){
        UUID announceId = UUID.randomUUID();
        UUID originalId = UUID.randomUUID();
        UUID fakeId = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setUserId(originalId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        assertThrows(NoAccesToChangeDataException.class, () ->
                service.update(new AnnouncementUpdateDto(), announceId, fakeId));
    }

    @Test
    void shouldPassVerification_in_update(){
        UUID announceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AnnouncementUpdateDto dto = new AnnouncementUpdateDto();

        Announcement announcement = new Announcement();
        announcement.setUserId(userId);
        announcement.setId(announceId);
        when(repo.findById(announceId)).thenReturn(Optional.of(announcement));

        service.update(dto, announceId, userId);
        verify(repo, times(1)).save(announcement);
        verify(requestMapper, times(1)).update(dto, announcement);
    }
}