package ilya.pon.search.service;

import ilya.pon.search.document.Announcement;
import ilya.pon.search.repository.AnnouncementRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AnnouncementService {
    private final ElasticsearchOperations operations;
    private final AnnouncementRepository repo;

    public void save(Announcement announcement) {
        repo.save(announcement);
    }

    public Announcement findById(@NotNull String id) {
        return repo.findById(id).orElse(null);
    }

    public Page<Announcement> search(@NotNull String text, Pageable pageable) {
        return repo.search(text, pageable);
    }

    public Page<Announcement> getAll(@NotNull Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Announcement> getByCategoryName(@NotBlank String categoryName, @NotNull Pageable pageable) {
        return repo.findByCategoryNameOrderByCreatedAtDesc(categoryName, pageable);
    }

    public  Page<Announcement> getByCategoryId(@NotBlank String categoryId, @NotNull Pageable pageable) {
        return repo.findByCategoryIdsContainingOrderByCreatedAtDesc(categoryId, pageable);
    }
}