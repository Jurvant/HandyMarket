package ilya.pon.search.repository;

import ilya.pon.search.document.Announcement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnnouncementRepository extends ElasticsearchRepository<Announcement, UUID> {

}
