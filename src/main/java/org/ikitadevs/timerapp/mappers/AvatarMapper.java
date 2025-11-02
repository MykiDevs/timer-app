package org.ikitadevs.timerapp.mappers;


import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.entities.Avatar;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AvatarMapper {
    @Mapping(source = "user.uuid", target = "user_uuid")
    AvatarResponseDto toAvatarResponseDto(Avatar avatar);
}
