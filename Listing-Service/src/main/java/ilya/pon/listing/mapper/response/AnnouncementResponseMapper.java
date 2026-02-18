package ilya.pon.listing.mapper.response;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnnouncementResponseMapper {

    @Mapping(target = "categoryName", source = "category.name")
    AnnouncementResponseDto toDto(Announcement announcement);
}