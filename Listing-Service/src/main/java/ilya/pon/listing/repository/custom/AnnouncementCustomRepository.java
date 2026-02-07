package ilya.pon.listing.repository.custom;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AnnouncementCustomRepository {
    Page<Announcement> findByParameters(UUID id, UUID userId, String title, String description, BigDecimal price,
                                        Status status, Category category, LocalDateTime createdAt, Pageable pageable);
    Page<Announcement> search(String text, Pageable pageable);
}