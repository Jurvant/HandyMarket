package ilya.pon.listing.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ilya.pon.listing.domain.QAnnouncement.announcement;

@Repository
public class AnnouncementCustomRepositoryImpl implements AnnouncementCustomRepository {
    private final JPAQueryFactory queryFactory;

    public AnnouncementCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Announcement> findByParameters(
            UUID id, UUID userId, String title, String description,
            BigDecimal price, Status status, Category category,
            LocalDateTime createdAt, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder();

        if (id != null) predicate.and(announcement.id.eq(id));
        if (userId != null) predicate.and(announcement.userId.eq(userId));
        if (title != null) predicate.and(announcement.title.containsIgnoreCase(title));
        if (description != null) predicate.and(announcement.description.containsIgnoreCase(description));
        if (price != null) predicate.and(announcement.price.eq(price));
        if (status != null) predicate.and(announcement.status.eq(status));
        if (category != null) predicate.and(announcement.category.eq(category));
        if (createdAt != null) predicate.and(announcement.createdAt.goe(createdAt));

        List<Announcement> results = queryFactory
                .selectFrom(announcement)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(announcement)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
