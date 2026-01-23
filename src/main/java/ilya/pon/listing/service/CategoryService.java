package ilya.pon.listing.service;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public Category findById(UUID categoryId) {
        return repo.findById(categoryId).orElseThrow(EntityNotFoundException::new);
    }
}