package ilya.pon.listing.controller;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @PostMapping("/new-announcement")
    public Announcement createAnnouncement(
            @RequestHeader("X-User-Id") UUID userId, @RequestBody AnnouncementCreateDto dto) {
        dto.setUserId(userId);
        return service.save(dto);
    }
}