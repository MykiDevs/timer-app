package org.ikitadevs.timerapp.mappers;


import org.ikitadevs.timerapp.dto.response.AvatarResponseDto;
import org.ikitadevs.timerapp.entities.Avatar;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AvatarMapper {
    AvatarResponseDto toAvatarResponseDto(Avatar avatar, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);
}
