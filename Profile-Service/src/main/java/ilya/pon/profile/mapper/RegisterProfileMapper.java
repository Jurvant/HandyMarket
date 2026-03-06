package ilya.pon.profile.mapper;

import ilya.pon.profile.domain.UserProfile;
import ilya.pon.profile.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterProfileMapper {

    @Mapping(target = "id", ignore = true)
    UserProfile toEntity(RegisterDto dto);
}