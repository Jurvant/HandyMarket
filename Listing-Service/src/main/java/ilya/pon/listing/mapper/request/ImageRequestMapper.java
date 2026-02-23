package ilya.pon.listing.mapper.request;

import ilya.pon.listing.domain.Image;
import ilya.pon.listing.dto.request.ImageCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageRequestMapper {
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    Image toEntity(ImageCreateDto dto);
}
