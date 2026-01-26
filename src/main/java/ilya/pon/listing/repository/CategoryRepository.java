package ilya.pon.listing.repository;

import ilya.pon.listing.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = "SELECT EXISTS(SELECT 1 FROM categories)", nativeQuery = true)
    boolean existsAnyNative();
}