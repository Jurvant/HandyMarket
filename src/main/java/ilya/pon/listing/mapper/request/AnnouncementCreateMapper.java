package ilya.pon.listing.mapper.request;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.service.CategoryService;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnnouncementCreateMapper {

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "getCategory")
    Announcement toEntity(AnnouncementCreateDto dto, @Context CategoryService service);

    @Named("getCategory")
    default Category getCategory(@Context CategoryService service, String categoryId){
        return service.findById(categoryId);
    }
}