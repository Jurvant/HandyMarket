package ilya.pon.listing.controller;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.service.AnnouncementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("deactivate-announcement/{id}")
    public void deactivateAnnouncement(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID id) {
        service.deactivateAnouncement(id, userId);
    }

    @DeleteMapping("/announcement/{id}")
    public void deleteAnnouncement(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        service.deleteById(id, userId);
    }

    @GetMapping("announcement")
    public Page<Announcement> findByParameters(AnnouncementFilterDto dto, Pageable pageable) {
        return service.findByParameters(dto, pageable);
    }
}