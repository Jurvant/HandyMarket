package ilya.pon.listing.mapper.request;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.service.CategoryService;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnnouncementRequestMapper {

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "getCategory")
    Announcement toEntity(AnnouncementCreateDto dto, @Context CategoryService service);

    @Named("getCategory")
    default Category getCategory(@Context CategoryService service, String categoryId){
        return service.findById(categoryId);
    }

    default void update(AnnouncementUpdateDto dto, Announcement announcement){
        Optional.ofNullable(dto.getTitle()).ifPresent(announcement::setTitle);
        Optional.ofNullable(dto.getDescription()).ifPresent(announcement::setDescription);
        Optional.ofNullable(dto.getPrice()).ifPresent(announcement::setPrice);
        Optional.ofNullable(dto.getCategory()).ifPresent(announcement::setCategory);
        Optional.ofNullable(dto.getStatus()).ifPresent(announcement::setStatus);
    }
}