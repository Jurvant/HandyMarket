package ilya.pon.search.controller;

import ilya.pon.search.document.Announcement;
import ilya.pon.search.service.AnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/search-service/announcement")
@AllArgsConstructor
public class AnnouncementController {
    private final AnnouncementService service;

    @PostMapping
    public void save(@RequestBody Announcement announcement) {
        service.save(announcement);
    }

    @GetMapping("/id")
    public Announcement findById(@RequestParam UUID id) {
        return service.findById(id);
    }
}
