package ilya.pon.listing.data.generation.category;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.dto.initial.CategoryInitialDto;
import ilya.pon.listing.mapper.initial.CategoryInitialMapper;
import ilya.pon.listing.service.CategoryService;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
public class CategoriesPusher {
    private final CategoryService categoryService;
    private final CategoryInitialMapper categoryMapper;
    public CategoriesPusher(CategoryService categoryService, CategoryInitialMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    public void readAndPush(){
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("db/files/categories.json");
        if (is == null) {
            throw new RuntimeException("No category file in resources/db/files");
        }

        ObjectMapper mapper = new ObjectMapper();
        List<CategoryInitialDto> list = mapper.readValue(is, new TypeReference<>() {});
        List<Category> categories = categoryMapper.toEntity(list);
        categoryService.saveAll(categories);
    }

    public boolean checkDatabase(){
        boolean result = categoryService.existsAny();
        System.out.println("Database check result: " + result);
        return result;
    }
}