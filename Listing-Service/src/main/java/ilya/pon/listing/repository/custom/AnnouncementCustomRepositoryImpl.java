package ilya.pon.listing.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
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
            UUID userId, String title, String description,
            BigDecimal price, Status status, Category category,
            LocalDateTime createdAt, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder();

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

        Long totalCount = queryFactory
                .select(announcement.count())
                .from(announcement)
                .where(predicate)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Announcement> search(String text, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        for (String p : text.trim().split("\\s+")) {
            if (!p.isBlank()) {
                predicate.and(announcement.title.containsIgnoreCase(p)
                        .or(announcement.description.containsIgnoreCase(p)));
            }
        }

        JPAQuery<Announcement> query = queryFactory
                .selectFrom(announcement)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Announcement> content = query.fetch();

        Long totalCount = queryFactory
                .select(announcement.count())
                .from(announcement)
                .where(predicate)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }
}