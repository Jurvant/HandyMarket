package ilya.pon.search.repository;

import ilya.pon.search.document.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnnouncementRepository extends ElasticsearchRepository<Announcement, String> {

    @Query("""
            {
              "multi_match": {
                "query": "?0",
                "fields": ["title^5", "description"]
              }
            }
            """)
    Page<Announcement> search(String text, Pageable pageable);

    Page<Announcement> findByCategoryNameOrderByCreatedAtDesc(String categoryName, Pageable pageable);

    Page<Announcement> findByCategoryIdsContainingOrderByCreatedAtDesc(String categoryId, Pageable pageable);
}