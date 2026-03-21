package ilya.pon.search.controller;

import ilya.pon.search.document.Announcement;
import ilya.pon.search.service.AnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("search-service/announcement")
@AllArgsConstructor
public class AnnouncementController {
    private final AnnouncementService service;

    @PostMapping
    public void save(@RequestBody Announcement announcement) {
        service.save(announcement);
    }

    @GetMapping("/{id}")
    public Announcement findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/search")
    public Page<Announcement> search(@RequestParam String title, @PageableDefault(size = 20) Pageable pageable) {
        return service.search(title, pageable);
    }

    @GetMapping
    public Page<Announcement> getAll(@PageableDefault(size = 50) Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/category")
    public Page<Announcement> getByCategoryName(@RequestParam String categoryName, @PageableDefault(size = 20) Pageable pageable) {
        return service.getByCategoryName(categoryName,  pageable);
    }

    @GetMapping("/category/id")
    public Page<Announcement> getByCategoryId(@RequestParam String categoryId, @PageableDefault(size = 20) Pageable pageable) {
        return service.getByCategoryId(categoryId, pageable);
    }


}