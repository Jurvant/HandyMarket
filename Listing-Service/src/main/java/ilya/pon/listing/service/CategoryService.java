package ilya.pon.listing.service;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public Category findById(String categoryId) {
        return repo.findById(categoryId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Category> saveAll(@NotEmpty @NotNull List<@NotNull Category> categories) {
        return repo.saveAll(categories);
    }

    public boolean existsAny(){
        return repo.existsAnyNative();
    }
}