package ilya.pon.listing.data.generation.category;

import ilya.pon.listing.config.properties.GenerationProperties;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.dto.initial.CategoryInitialDto;
import ilya.pon.listing.mapper.initial.CategoryInitialMapper;
import ilya.pon.listing.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
public class CategoriesPusher {

    Logger log = LoggerFactory.getLogger(CategoriesPusher.class);

    private final CategoryService categoryService;
    private final CategoryInitialMapper categoryMapper;
    private final GenerationProperties properties;

    public CategoriesPusher(CategoryService categoryService, CategoryInitialMapper categoryMapper, GenerationProperties properties) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.properties = properties;
    }

    public void readAndPush(){
        log.info("Reading categories source file");
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream(properties.getCategoriesPath());
        if (is == null) {
            log.error("No category file in {}", properties.getCategoriesPath());
            throw new RuntimeException();
        }

        log.info("Converting Json to entities");
        ObjectMapper mapper = new ObjectMapper();
        List<CategoryInitialDto> list = mapper.readValue(is, new TypeReference<>() {});
        List<Category> categories = categoryMapper.toEntity(list);
        log.info("Saving all pulled categories");
        categoryService.saveAll(categories);
    }

    public boolean checkDatabase(){
        log.info("Checking database for any record");
        boolean result = categoryService.existsAny();
        System.out.println("Database empty: " + result);
        return result;
    }
}