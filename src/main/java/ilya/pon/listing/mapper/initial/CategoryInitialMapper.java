package ilya.pon.listing.mapper.initial;

import ilya.pon.listing.domain.Category;
import ilya.pon.listing.dto.initial.CategoryInitialDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryInitialMapper {

    List<Category> toEntity(List<CategoryInitialDto> list);
}
