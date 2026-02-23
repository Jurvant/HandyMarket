package ilya.pon.listing.mapper.response;

import ilya.pon.listing.domain.Image;
import ilya.pon.listing.dto.response.ImageResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageResponseMapper {
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "id", source = "id")
    ImageResponseDto toDto(Image image);
}