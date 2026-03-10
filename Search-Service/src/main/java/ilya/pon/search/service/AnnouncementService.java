package ilya.pon.search.service;

import ilya.pon.search.document.Announcement;
import ilya.pon.search.repository.AnnouncementRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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

    public Announcement findById(@NotNull UUID id) {
        return repo.findById(id).orElse(null);
    }


}
